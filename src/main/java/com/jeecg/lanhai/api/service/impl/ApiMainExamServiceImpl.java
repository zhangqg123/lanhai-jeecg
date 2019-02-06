package com.jeecg.lanhai.api.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.jeecgframework.core.util.HttpClientUtil;
import org.jeecgframework.web.linksucai.oauth2.qiye.pojo.AccessToken;
import org.jeecgframework.web.linksucai.oauth2.qiye.util.WechatAccessToken;
import org.jeecgframework.web.system.pojo.base.TSNoticeReadUser;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.jeecg.account.entity.LhSAccountEntity;
import com.jeecg.account.service.LhSAccountService;
import com.jeecg.exam.entity.LhExamScoreEntity;
import com.jeecg.exam.service.LhExamService;
import com.jeecg.lanhai.activiti.util.WXTemplate;
import com.jeecg.lanhai.activiti.util.WXTemplateData;
import com.jeecg.lanhai.api.entity.FormTemplateVO;
import com.jeecg.lanhai.api.service.ApiMainExamService;
import com.jeecg.zwzx.entity.WorkApplyEntity;

@Service("apiMainExamService")
public class ApiMainExamServiceImpl implements ApiMainExamService {
	@Resource
	private RedisTemplate<String,FormTemplateVO> redisTemplate;
	@Autowired
	private LhExamService lhExamService;
	@Autowired
	private LhSAccountService lhSAccountService;
	private ExecutorService executor = Executors.newCachedThreadPool();
	
	private static final String SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send";
    private final String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private String templateId="OZy8F0P10K3RAsgYEy5a_LebF9WjMeLDYF5hbq70YK0";

	public void collect(String openId, List<FormTemplateVO> formTemplates) {
	    redisTemplate.opsForList().rightPushAll("mina:openid:" + openId, formTemplates);

	}
	
	public String getValidFormId(String openId) {
	    // 移除本次使用的和已过期的
	    List<FormTemplateVO> formTemplates = redisTemplate.opsForList().range("mina:openid:" + openId, 0, -1);

	    String validFormId = "";
	    int trimStart = 0;
	    int size=formTemplates.size();
	    for (int i = 0; i < size; i++) {
	    	FormTemplateVO formTemplate = (FormTemplateVO) formTemplates.get(i);
	    	long expireTime = formTemplate.getExpireTime();
	    	long date = System.currentTimeMillis();
	        if (expireTime > date) {
	            validFormId = formTemplate.getFormId();
	            trimStart = i + 1;
	            break;
	        }
	    }

	    // 移除本次使用的和已过期的
	    redisTemplate.opsForList().trim("mina:openid:" + openId, trimStart == 0 ? size : trimStart, -1);
	    return validFormId;
	}

	@Override
	@Transactional
	public JSONArray reply(String param, String openId, String examId,final String appId) {
		Map<String, Object> ret = lhExamService.countScore(param,openId,examId);
		JSONArray jsonArray=(JSONArray) ret.get("jsonArray");
		final LhExamScoreEntity lhExamScore=(LhExamScoreEntity) ret.get("lhExamScore");
		final String formId = getValidFormId(openId);
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
			    sendTemplateMessage(lhExamScore,formId,appId);
			}
		});
//	    sendTemplateMessage(lhExamScore,formId,appId);
		return jsonArray;
	}
	
	private void sendTemplateMessage(LhExamScoreEntity lhExamScore,String formId,String appId) {
		String openId=lhExamScore.getOpenId();
		LhSAccountEntity lhSAccount = lhSAccountService.getByAppId(appId);
		String secret=lhSAccount.getAppSecret();

		if(openId!=null){
	    	Map<String, WXTemplateData> map = new HashMap<String, WXTemplateData>();
	    	Integer score = lhExamScore.getScore();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	        String auditDate = sdf.format(new Date());  
	        map.put("keyword1", new WXTemplateData(String.valueOf(score),"#173177"));
	        map.put("keyword2", new WXTemplateData(lhExamScore.getExamId(),"#173177"));
	        map.put("keyword3", new WXTemplateData(auditDate,"#173177"));
	        map.put("keyword4", new WXTemplateData(lhExamScore.getNumber().toString(),"#173177"));
			AccessToken accessToken = WechatAccessToken.getAccessToken(appId,secret, 0);
			String token=accessToken.getToken();
			String requestUrl = SEND_URL + "?access_token="+token;
			
			WXTemplate template = new WXTemplate();
			template.setTouser(openId);
			template.setTemplate_id(templateId);
			template.setPage("pages/home/myproject");
			template.setForm_id(formId);
			template.setData(map);
			template.setEmphasis_keyword("keyword1.DATA");
	
			String jsonMsg = JSONObject.fromObject(template).toString();
//			System.out.println("发送模板消息：" + jsonMsg);
			// 发送模板消息
			String resp = HttpClientUtil.post(requestUrl, jsonMsg, null);
//			System.out.println("模板消息返回：" + resp);
		}
	}
	
//    public  String getAccessToken(String appId ,String appSecret){
//        
//        String url = accessTokenUrl.replace("APPID", appId);
//        url = url.replace("APPSECRET", appSecret);
//        JSONObject resultJson =null;
//        String result = HttpClientUtil.post(url, "", null);
//         try {
//             resultJson = JSONObject.fromObject(result);
////             System.out.println("access token 返回值：" + resultJson);
//         } catch (JSONException e) {
//             e.printStackTrace();
//         }
//         
//         return (String) resultJson.get("access_token");
//
//    }

}
