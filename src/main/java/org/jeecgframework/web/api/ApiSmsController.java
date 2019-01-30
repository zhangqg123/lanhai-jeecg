package org.jeecgframework.web.api;

import java.io.IOException;
import java.net.URLDecoder;
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
import org.jeecgframework.web.api.service.SmsLoginService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.jeecg.account.entity.LhSAccountEntity;
import com.jeecg.account.service.LhSAccountService;
import com.jeecg.zwzx.entity.WorkBlacklistEntity;
import com.jeecg.zwzx.entity.WorkMenuEntity;
import com.jeecg.zwzx.entity.WorkUserEntity;
import com.jeecg.zwzx.service.WorkBlacklistService;
import com.jeecg.zwzx.service.WorkGuideService;
import com.jeecg.zwzx.service.WorkUserService;
import com.jeecg.zwzx.utils.AES128Util;
import com.jeecg.zwzx.utils.PasswordUtil;

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
	private SmsLoginService smsLoginService;
	@Autowired
	private WorkUserService workUserService;
	@Autowired
	private WorkBlacklistService workBlacklistService;
	@Autowired
	private LhSAccountService lhSAccountService;
	
	@RequestMapping(value="/smsCode")
	public @ResponseBody AjaxJson txsms(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxJson j = new AjaxJson();
    	String phone=request.getParameter("phone");
    	String usertype=request.getParameter("usertype");
    	if(phone==null){
    		j.setSuccess(false);
    		return j;
    	}
		String appId=request.getParameter("xcxId");
		String smsResult=smsLoginService.sendSms(appId,phone,usertype);
		if(smsResult.equals("success")){
			j.setSuccess(true);
		}else{
			j.setSuccess(false);
			j.setMsg(smsResult);
		}
		return j;
	}
	
	@RequestMapping("/smsCodeLogin")
	public @ResponseBody AjaxJson smsCodeLogin(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
    	String phone=request.getParameter("phone");
    	String userkey=request.getParameter("userkey");
    	String openid=request.getParameter("openId");
    	String usertype=request.getParameter("usertype");
    	WorkUserEntity workUser=new WorkUserEntity();
		try {
	    	if(phone!=null&&userkey!=null){
	        	workUser.setPhone(phone);
	        	workUser.setUserkey(userkey);
	        	workUser.setUsertype(usertype);
				MiniDaoPage<WorkUserEntity> list = workUserService.getAll(workUser, 1, 10);
				List<WorkUserEntity> workUserList = list.getResults();
				if(workUserList.size()>0){
					workUser=workUserList.get(0);
					// 2，短信验证码登录
					workUser.setStatus(2);
					workUser.setOpenid(openid);
					workUserService.update(workUser);
					j.setObj(workUser.getId());
					Map<String,Object> attributes=new HashMap<String,Object>();
					attributes.put("status", 2);
					j.setAttributes(attributes);
					j.setSuccess(true);
				}else{
					j.setSuccess(false);
				}
	    	}else{
				j.setSuccess(false);	    		
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
		}
		return j;
	}
	
	@RequestMapping(value ="/userRegister", method = RequestMethod.POST)
	public @ResponseBody AjaxJson userRegister(HttpServletRequest request, @RequestBody WorkUserEntity workUser) {
		AjaxJson j = new AjaxJson();
		String password = workUser.getPassword();
		try {
			workUser=workUserService.get(workUser.getId());
			Map<String,Object> attributes=new HashMap<String,Object>();
			workUser.setPassword(PasswordUtil.encrypt(workUser.getUsername(), password, PasswordUtil.getStaticSalt()));
			workUser.setStatus(3);
			workUserService.update(workUser);
			j.setObj(workUser.getId());
			attributes.put("status", 3);
			j.setAttributes(attributes);
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
		}
		return j;
	}
	
	@RequestMapping("/idCardLogin")
	public @ResponseBody AjaxJson idCardLogin(HttpServletRequest request, HttpServletResponse response) {
		//TODO 验证身份证号接口
		
		AjaxJson j = new AjaxJson();
		try {
    		WorkUserEntity workUser=new WorkUserEntity();
			String idcard=request.getParameter("idcard");
			String usertype=request.getParameter("usertype");
			workUser.setIdcard(idcard);
			workUser.setUsertype(usertype);
			MiniDaoPage<WorkUserEntity> list = workUserService.getAll(workUser, 1, 10);
			List<WorkUserEntity> workUserList = list.getResults();
			if(workUserList.size()>0){
				j.setSuccess(false);
				j.setMsg("身份证号已注册");
				return j;
			}
    		String id = request.getHeader("login-code");
    		workUser.setId(id);
			String realname= URLDecoder.decode(request.getParameter("realname"),"utf-8");
			
			workUser=workUserService.get(workUser.getId());
			workUser.setRealname(realname);
			workUser.setIdcard(idcard);
			// 4，身份证验证通过
			workUser.setStatus(4);
			workUserService.update(workUser);
			j.setObj(workUser.getId());
			Map<String,Object> attributes=new HashMap<String,Object>();
			attributes.put("status", 4);
			j.setAttributes(attributes);
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
		}
		return j;
	}
	
	@RequestMapping(value= "/login", method = RequestMethod.POST)
	public @ResponseBody AjaxJson login(HttpServletRequest request, @RequestBody WorkUserEntity workUser) {
		AjaxJson j = new AjaxJson();
		if(workUser.getUsername()!=null&&workUser.getPassword()!=null){
			String appId=request.getParameter("xcxId");
			String encryptPass="";
			try{
				LhSAccountEntity lhSAccount = lhSAccountService.getByAppId(appId);
				encryptPass = AES128Util.decrypt(workUser.getPassword(), lhSAccount.getAesKey() ,lhSAccount.getIvKey());
				workUser.setPassword(PasswordUtil.encrypt(workUser.getUsername(), encryptPass, PasswordUtil.getStaticSalt()));

				MiniDaoPage<WorkUserEntity> list = workUserService.getAll(workUser, 1, 10);
				List<WorkUserEntity> workUserList = list.getResults();
				if(workUserList.size()==1){
					System.out.println("workUserList.size:"+workUserList.size()+"个");
					workUser=workUserList.get(0);
					
					Map<String,Object> attributes=new HashMap<String,Object>();
					attributes.put("login_code", workUser.getId());
					attributes.put("status", workUser.getStatus());
					j.setAttributes(attributes);
					j.setSuccess(true);
				}else{
					j.setSuccess(false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				j.setSuccess(false);
			}
		}else{
			j.setSuccess(false);
		}
		return j;
	}
	
	@RequestMapping("/userInfo")
	public @ResponseBody AjaxJson userInfo(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		String id = request.getHeader("login-code");
		if(id!=null&&id!=""){
			try {
	    		System.out.println("id:"+id);
	    		WorkUserEntity workUser=new WorkUserEntity();
	    		workUser.setId(id);
				MiniDaoPage<WorkUserEntity> list = workUserService.getAll(workUser, 1, 10);
				List<WorkUserEntity> workUserList = list.getResults();
				if(workUserList.size()>0){
		    		System.out.println("workUserList.size():-----"+workUserList.size());
					workUser=workUserList.get(0);
	//				Map<String,Object> attributes=new HashMap<String,Object>();
	//				attributes.put("status", workUser.getStatus());
	//				j.setAttributes(attributes);
					j.setObj(workUser);
					j.setSuccess(true);
				}else{
					j.setSuccess(false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				j.setSuccess(false);
			}
		}else{
			j.setSuccess(false);
		}
		return j;
	}
	
}
