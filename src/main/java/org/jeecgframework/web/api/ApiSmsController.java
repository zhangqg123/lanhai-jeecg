package org.jeecgframework.web.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.jeecgframework.minidao.pojo.MiniDaoPage;
import org.jeecgframework.p3.core.common.utils.AjaxJson;
import org.jeecgframework.p3.core.page.SystemTools;
import org.jeecgframework.p3.core.util.oConvertUtils;
import org.jeecgframework.p3.core.util.plugin.ViewVelocity;
import org.jeecgframework.p3.core.web.BaseController;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.jeecg.zwzx.entity.WorkBlacklistEntity;
import com.jeecg.zwzx.entity.WorkMenuEntity;
import com.jeecg.zwzx.entity.WorkUserEntity;
import com.jeecg.zwzx.service.WorkBlacklistService;
import com.jeecg.zwzx.service.WorkGuideService;
import com.jeecg.zwzx.service.WorkUserService;

/**
 * CMS API
 * 
 * @author zhangdaihao
 * 
 */
@Controller
@RequestMapping("/api/txsms")
public class ApiSmsController extends BaseController {
	@Autowired
	private WorkUserService workUserService;
	@Autowired
	private WorkBlacklistService workBlacklistService;

	@RequestMapping(value="/smsCode")
	public @ResponseBody AjaxJson txsms(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxJson j = new AjaxJson();
    	String phone=request.getParameter("phone");
    	if(phone==null){
    		j.setSuccess(false);
    		return j;
    	}
    	WorkBlacklistEntity workBlacklist=new WorkBlacklistEntity();
    	workBlacklist.setPhone(phone);
    	MiniDaoPage<WorkBlacklistEntity> blackList = workBlacklistService.getAll(workBlacklist, 1, 10);
     	if(blackList.getResults().size()>0){
     		j.setSuccess(false);
     		j.setMsg("黑名单");
     		return j;
     	}
    	WorkUserEntity workUser=new WorkUserEntity();
    	workUser.setPhone(phone);
		MiniDaoPage<WorkUserEntity> list = workUserService.getAll(workUser, 1, 10);
		List<WorkUserEntity> workUserList = list.getResults();
		if(workUserList.size()>0){
			workUser=workUserList.get(0);
		}
		if(workUser.getPassword()!=null){
			j.setSuccess(false);
			j.setMsg("号码已注册");
		}else{
			// 短信应用SDK AppID
			int appid = 1400136389; // 1400开头
	
			// 短信应用SDK AppKey
			String appkey = "bc375159dc3b6f67ac9967eb42cef49b";
	
			// 需要发送短信的手机号码
			String[] phoneNumbers = {workUser.getPhone()};
	
			// 短信模板ID，需要在短信应用中申请
			int templateId = 201454; // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
			//templateId7839对应的内容是"您的验证码是: {1}"
			// 签名
			String smsSign = "张庆国朦胧岁月"; // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`
			try {
				//创建验证码
				Random r = new Random();
				StringBuffer str = new StringBuffer();
				//验证码位数
				Integer num = 6;//默认6位
				int i = 0;
				while(i < num) {
					str.append(r.nextInt(10));
					i++;
				}
				String values = str.toString();
	            String[] params = {values,"10"};
	            // 调试时关闭发送短信
	            SmsMultiSender msender = new SmsMultiSender(appid, appkey);
	            SmsMultiSenderResult result =  msender.sendWithParam("86", phoneNumbers,
	                templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
				System.out.println("result:"+result);
	            if (result.result==0) {
		    		if(workUser.getId()!=null){
						workUser.setUserkey(values);
		    			workUser.setStatus(1);
						workUserService.update(workUser);
		    		}else{
		    			workUser.setUsername(workUser.getPhone());
		    			workUser.setUserkey(values);
		    			// 1,已发送验证码
		    			workUser.setStatus(1);
		    			workUserService.insert(workUser);
					}
					j.setSuccess(true);
				}else{
					j.setSuccess(false);
					j.setMsg(result.toString());
				}
			} catch (HTTPException e) {
			    e.printStackTrace();
				j.setSuccess(false);
			} catch (JSONException e) {
			    e.printStackTrace();
				j.setSuccess(false);
	//		} catch (IOException e) {
	//		    e.printStackTrace();
			}
		}
		return j;
	}
	
}
