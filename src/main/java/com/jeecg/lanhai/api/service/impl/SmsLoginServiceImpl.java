package com.jeecg.lanhai.api.service.impl;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.jeecgframework.minidao.pojo.MiniDaoPage;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.jeecg.lanhai.api.service.SmsLoginService;
import com.jeecg.lhs.account.dao.LhSAccountDao;
import com.jeecg.lhs.account.entity.LhSAccountEntity;
import com.jeecg.user.entity.LhSUserEntity;
import com.jeecg.user.service.LhSUserService;

@Service("smsLoginService")
public class SmsLoginServiceImpl implements SmsLoginService {
	@Autowired
	private LhSUserService lhSUserService;
	@Resource
	private LhSAccountDao lhSAccountDao;

	@Override
	public String sendSms(String appId, String phone, String usertype, String status) {
		String msg=null;
		LhSAccountEntity lhSAccount = lhSAccountDao.getByAppId(appId);

    	LhSUserEntity lhSUser=new LhSUserEntity();
    	lhSUser.setPhone(phone);
    	lhSUser.setUsertype(usertype);
		MiniDaoPage<LhSUserEntity> list = lhSUserService.getAll(lhSUser, 1, 10);
		List<LhSUserEntity> lhSUserList = list.getResults();
		if(lhSUserList.size()>0){
			lhSUser=lhSUserList.get(0);
		}
		if(status.equals("smsCode")&&lhSUser.getPassword()!=null){
			msg= "号码已注册";
		}else{
			// 短信应用SDK AppID
			int appid = Integer.valueOf(lhSAccount.getSmsAppid()); // 1400开头
	
			// 短信应用SDK AppKey
			String appkey = lhSAccount.getSmsAppkey();
	
			// 需要发送短信的手机号码
			String[] phoneNumbers = {lhSUser.getPhone()};
	
			// 短信模板ID，需要在短信应用中申请
			int templateId = Integer.valueOf(lhSAccount.getSmsTemplateid()); // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
			//templateId7839对应的内容是"您的验证码是: {1}"
			// 签名
			String smsSign = "延吉浪潮"; // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`
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
					if(status.equals("smsCode")){
		    			lhSUser.setStatus(1);		// 1,已发送验证码					
					}
		    		if(lhSUser.getId()!=null){
						lhSUser.setUserkey(values);
						lhSUserService.update(lhSUser);
		    		}else{
		    			lhSUser.setUsername(lhSUser.getPhone());
		    			lhSUser.setUserkey(values);
		    			
		    			lhSUserService.insert(lhSUser);
					}
					msg="success";
				}else{
					msg=result.toString();
				}
			} catch (HTTPException e) {
			    e.printStackTrace();
				msg="false";
			} catch (JSONException e) {
			    e.printStackTrace();
				msg="false";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg="false";
			}
		}
		return msg;
	}
	
}
