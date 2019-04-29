package com.jeecg.lanhai.lhs.service.impl;

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
import org.jeecgframework.minidao.pojo.MiniDaoPage;
import org.jeecgframework.web.linksucai.oauth2.qiye.pojo.AccessToken;
import org.jeecgframework.web.linksucai.oauth2.qiye.util.WechatAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.jeecg.ask.entity.AskStatusEntity;
import com.jeecg.ask.entity.LhDsAskEntity;
import com.jeecg.ask.service.LhDsAskService;
import com.jeecg.ask.utils.LstConstants;
import com.jeecg.exam.entity.LhExamScoreEntity;
import com.jeecg.exam.service.LhExamService;
import com.jeecg.lanhai.activiti.util.WXTemplate;
import com.jeecg.lanhai.activiti.util.WXTemplateData;
import com.jeecg.lanhai.lhs.entity.FormTemplateVO;
import com.jeecg.lanhai.lhs.service.LhsService;
import com.jeecg.lhs.entity.LhSAccountEntity;
import com.jeecg.lhs.entity.LhSUserEntity;
import com.jeecg.lhs.service.LhSAccountService;
import com.jeecg.lhs.service.LhSUserService;

@Service("lhsService")
public class LhsServiceImpl implements LhsService {
	@Resource
	private RedisTemplate<String,FormTemplateVO> redisTemplate;
	@Autowired
	private LhExamService lhExamService;
	@Autowired
	private LhSAccountService lhSAccountService;
	@Autowired
	private LhSUserService lhSUserService;
	@Autowired
	private LhDsAskService lhDsAskService;
	
	private ExecutorService executor = Executors.newCachedThreadPool();
	
	private static final String SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send";
//    private final String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private String templateId="weOSnK8qqB532FN31FOn1gKN6Q1e-1OEYJPWhWE4-wY";
	private String createTemplateId="Ejd0HlZC_fTQIRJjbbuHstZ7VHo0FwWzvJK-YRIxN68";
	private String auditTemplateId="6_mxEbFQ4q5PktBQ2LcXWTIRdRvepywFbjDXOROgTts";
	
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
	public JSONArray reply(String param, String openId, String examId,final String appId,final String useTime) {
		Map<String, Object> ret = lhExamService.countScore(param,openId,examId);
		JSONArray jsonArray=(JSONArray) ret.get("jsonArray");
		final LhExamScoreEntity lhExamScore=(LhExamScoreEntity) ret.get("lhExamScore");
//		final String formId = getValidFormId(openId);
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
			    sendTemplateMessage(lhExamScore,appId,useTime);
			}
		});
//	    sendTemplateMessage(lhExamScore,formId,appId);
		return jsonArray;
	}
	
	private void sendTemplateMessage(LhExamScoreEntity lhExamScore,String appId,String useTime) {
		String openId=lhExamScore.getOpenId();
		LhSUserEntity lhSUser=new LhSUserEntity();
		lhSUser.setOpenid(openId);
		MiniDaoPage<LhSUserEntity> list = lhSUserService.getAll(lhSUser, 1, 10);
		List<LhSUserEntity> lhSUserList = list.getResults();
		String parentOpenIds=null;
		if(lhSUserList.size()>0){
			parentOpenIds = lhSUserList.get(0).getParent();
		}
		String formId = getValidFormId(openId);
//		String parentFormId=null;
		String[] poids=null;
		String[] pfids = null;;
		if(parentOpenIds!=null){
			poids = parentOpenIds.split(",");
			pfids=new String[poids.length];
	    	for(int i=0;i<poids.length;i++){
				pfids[i]=getValidFormId(poids[i]);
			}
		}
		LhSAccountEntity lhSAccount = lhSAccountService.getByAppId(appId);
		String secret=lhSAccount.getAppSecret();
		AccessToken accessToken = WechatAccessToken.getAccessToken(appId,secret, 0);
		String token=accessToken.getToken();
		
    	Map<String, WXTemplateData> map = new HashMap<String, WXTemplateData>();
    	Integer score = lhExamScore.getScore();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String auditDate = sdf.format(new Date());  
        map.put("keyword1", new WXTemplateData(String.valueOf(score)+"分","#173177"));
        map.put("keyword2", new WXTemplateData(lhExamScore.getExamName(),"#173177"));
        map.put("keyword3", new WXTemplateData(useTime,"#173177"));
        map.put("keyword4", new WXTemplateData(auditDate ,"#173177"));
		String requestUrl = SEND_URL + "?access_token="+token;
		
		if(formId!=null){	
			WXTemplate template = new WXTemplate();
			template.setTouser(openId);
			template.setTemplate_id(templateId);
			template.setPage("pages/home/myproject");
			template.setForm_id(formId);
			template.setData(map);
			template.setEmphasis_keyword("keyword1.DATA");
	
			String jsonMsg = JSONObject.fromObject(template).toString();
			// 发送模板消息
			String resp = HttpClientUtil.post(requestUrl, jsonMsg, null);
		}
		if(poids!=null && pfids!=null && poids.length>0 && pfids.length>0){
	    	for(int i=0;i<pfids.length;i++){
			
				WXTemplate template = new WXTemplate();
				template.setTouser(poids[i]);
				template.setTemplate_id(templateId);
				template.setPage("pages/home/myproject");
				template.setForm_id(pfids[i]);
				template.setData(map);
				template.setEmphasis_keyword("keyword1.DATA");
		
				String jsonMsg = JSONObject.fromObject(template).toString();
				// 发送模板消息
				String resp = HttpClientUtil.post(requestUrl, jsonMsg, null);
			}
			
		}

	}

	@Override
	public void sendWeChat(final LhDsAskEntity lstAsk, final String xcxId) {
//		lhDsAskService.insert(lstAsk);
		executor.execute(new Runnable() {			
			@Override
			public void run() {
			    sendMessage(lstAsk,xcxId);
			}
		});
		
	}
	
	private void sendMessage(LhDsAskEntity lstAsk, String xcxId) {
//		LhSUserEntity lhSUser=new LhSUserEntity();
		Integer status = lstAsk.getAskStatus();
		String openId=null;
		if(status==LstConstants.CREATE_ASK || status==LstConstants.AUDIT_ASK || status==LstConstants.ASK_DENY){
			openId = lstAsk.getAskOpenId();
		}
		if(lstAsk.getAskStatus()==LstConstants.TRANS_ASK || lstAsk.getAskStatus()==LstConstants.TRANS_ANSWER){
			openId=lstAsk.getDealOpenId();
		}
		if(status==LstConstants.ANSWER_ASK || status==LstConstants.AUDIT_ANSWER || status==LstConstants.ANSWER_DENY){
			openId = lstAsk.getAnswerOpenId();
		}
		String sendMessage=null;
		List<AskStatusEntity> statusList = LstConstants.getStatusList();
		for(AskStatusEntity as : statusList){
			if(as.getId()==status){
				sendMessage=as.getStatusName();
			}
		}
		
		if(openId!=null){
//			lhSUser.setOpenid(openId);
			String formId = getValidFormId(openId);
			LhSAccountEntity lhSAccount = lhSAccountService.getByAppId(xcxId);
			String secret=lhSAccount.getAppSecret();
			AccessToken accessToken = WechatAccessToken.getAccessToken(xcxId,secret, 0);
			String token=accessToken.getToken();
			
	    	Map<String, WXTemplateData> map = new HashMap<String, WXTemplateData>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	        String auditDate = sdf.format(new Date());  
	        map.put("keyword1", new WXTemplateData(sendMessage,"#173177"));
	        map.put("keyword2", new WXTemplateData(lstAsk.getAskTitle(),"#173177"));
	        map.put("keyword3", new WXTemplateData(auditDate,"#173177"));
			String requestUrl = SEND_URL + "?access_token="+token;
			String resp=null;
			if(formId!=null){	
				WXTemplate template = new WXTemplate();
				template.setTouser(openId);
				template.setTemplate_id(auditTemplateId);
				template.setPage("pages/home/myproject");
				template.setForm_id(formId);
				template.setData(map);
				template.setEmphasis_keyword("keyword1.DATA");
		
				String jsonMsg = JSONObject.fromObject(template).toString();
				// 发送模板消息
				resp = HttpClientUtil.post(requestUrl, jsonMsg, null);
			}
		}
	}
	
}
