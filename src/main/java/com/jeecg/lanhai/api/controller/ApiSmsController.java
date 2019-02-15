package com.jeecg.lanhai.api.controller;

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
import com.jeecg.lanhai.api.service.SmsLoginService;
import com.jeecg.lhs.account.entity.LhSAccountEntity;
import com.jeecg.lhs.account.service.LhSAccountService;
import com.jeecg.user.entity.LhSUserEntity;
import com.jeecg.user.service.LhSUserService;
import com.jeecg.user.utils.AES128Util;
import com.jeecg.user.utils.PasswordUtil;

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
	private LhSUserService lhSUserService;
	@Autowired
	private LhSAccountService lhSAccountService;
	
	@RequestMapping(value="/smsCode")
	public @ResponseBody AjaxJson txsms(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxJson j = new AjaxJson();
    	String phone=request.getParameter("phone");
    	String usertype=request.getParameter("usertype");
    	String status=request.getParameter("status");
    	if(phone==null){
    		j.setSuccess(false);
    		return j;
    	}
		String appId=request.getParameter("xcxId");
		String smsResult=smsLoginService.sendSms(appId,phone,usertype,status);
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
    	String xcxId=request.getParameter("xcxId");
    	String usertype=request.getParameter("usertype");
    	LhSUserEntity lhSUser=new LhSUserEntity();
		try {
	    	if(phone!=null&&userkey!=null){
	        	lhSUser.setPhone(phone);
	        	lhSUser.setUserkey(userkey);
	        	lhSUser.setUsertype(usertype);
				MiniDaoPage<LhSUserEntity> list = lhSUserService.getAll(lhSUser, 1, 10);
				List<LhSUserEntity> lhSUserList = list.getResults();
				if(lhSUserList.size()>0){
					lhSUser=lhSUserList.get(0);
					// 2，短信验证码登录
					lhSUser.setStatus(2);
					lhSUser.setOpenid(openid);
					lhSUser.setXcxid(xcxId);
					lhSUserService.update(lhSUser);
					j.setObj(lhSUser.getId());
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
	@RequestMapping("/follow")
	public @ResponseBody AjaxJson follow(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
    	String phone=request.getParameter("phone");
    	String userkey=request.getParameter("userkey");
    	String openid=request.getParameter("openId");
    	String usertype=request.getParameter("usertype");
    	LhSUserEntity lhSUser=new LhSUserEntity();
		try {
	    	if(phone!=null&&userkey!=null){
	        	lhSUser.setPhone(phone);
	        	lhSUser.setUserkey(userkey);
	        	lhSUser.setUsertype(usertype);
				MiniDaoPage<LhSUserEntity> list = lhSUserService.getAll(lhSUser, 1, 10);
				List<LhSUserEntity> lhSUserList = list.getResults();
				if(lhSUserList.size()>0){
					lhSUser=lhSUserList.get(0);
					// 2，短信验证码登录
					lhSUser.setParent(openid);
					lhSUserService.update(lhSUser);
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
	public @ResponseBody AjaxJson userRegister(HttpServletRequest request, @RequestBody LhSUserEntity lhSUser) {
		AjaxJson j = new AjaxJson();
		String password = lhSUser.getPassword();
		try {
			lhSUser=lhSUserService.get(lhSUser.getId());
			Map<String,Object> attributes=new HashMap<String,Object>();
			lhSUser.setPassword(PasswordUtil.encrypt(lhSUser.getUsername(), password, PasswordUtil.getStaticSalt()));
			lhSUser.setStatus(3);
			lhSUserService.update(lhSUser);
			j.setObj(lhSUser.getId());
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
    		LhSUserEntity lhSUser=new LhSUserEntity();
			String idcard=request.getParameter("idcard");
			String usertype=request.getParameter("usertype");
			lhSUser.setIdcard(idcard);
			lhSUser.setUsertype(usertype);
			MiniDaoPage<LhSUserEntity> list = lhSUserService.getAll(lhSUser, 1, 10);
			List<LhSUserEntity> lhSUserList = list.getResults();
			if(lhSUserList.size()>0){
				j.setSuccess(false);
				j.setMsg("身份证号已注册");
				return j;
			}
    		String id = request.getHeader("login-code");
    		lhSUser.setId(id);
			String realname= URLDecoder.decode(request.getParameter("realname"),"utf-8");
			
			lhSUser=lhSUserService.get(lhSUser.getId());
			lhSUser.setRealname(realname);
			lhSUser.setIdcard(idcard);
			// 4，身份证验证通过
			lhSUser.setStatus(4);
			lhSUserService.update(lhSUser);
			j.setObj(lhSUser.getId());
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
	public @ResponseBody AjaxJson login(HttpServletRequest request, @RequestBody LhSUserEntity lhSUser) {
		AjaxJson j = new AjaxJson();
		if(lhSUser.getUsername()!=null&&lhSUser.getPassword()!=null){
			String appId=request.getParameter("xcxId");
			String encryptPass="";
			try{
				LhSAccountEntity lhSAccount = lhSAccountService.getByAppId(appId);
				encryptPass = AES128Util.decrypt(lhSUser.getPassword(), lhSAccount.getAesKey() ,lhSAccount.getIvKey());
				lhSUser.setPassword(PasswordUtil.encrypt(lhSUser.getUsername(), encryptPass, PasswordUtil.getStaticSalt()));

				MiniDaoPage<LhSUserEntity> list = lhSUserService.getAll(lhSUser, 1, 10);
				List<LhSUserEntity> lhSUserList = list.getResults();
				if(lhSUserList.size()==1){
					System.out.println("lhSUserList.size:"+lhSUserList.size()+"个");
					lhSUser=lhSUserList.get(0);
					
					Map<String,Object> attributes=new HashMap<String,Object>();
					attributes.put("login_code", lhSUser.getId());
					attributes.put("status", lhSUser.getStatus());
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
	    		LhSUserEntity lhSUser=new LhSUserEntity();
	    		lhSUser.setId(id);
				MiniDaoPage<LhSUserEntity> list = lhSUserService.getAll(lhSUser, 1, 10);
				List<LhSUserEntity> lhSUserList = list.getResults();
				if(lhSUserList.size()>0){
		    		System.out.println("lhSUserList.size():-----"+lhSUserList.size());
					lhSUser=lhSUserList.get(0);
	//				Map<String,Object> attributes=new HashMap<String,Object>();
	//				attributes.put("status", lhSUser.getStatus());
	//				j.setAttributes(attributes);
					j.setObj(lhSUser);
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
	
	@RequestMapping("/userFollow")
	public @ResponseBody AjaxJson userFollow(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		String openId = request.getParameter("openId");
		if(openId!=null&&openId!=""){
			try {
	    		LhSUserEntity lhSUser=new LhSUserEntity();
	    		lhSUser.setParent(openId);
				MiniDaoPage<LhSUserEntity> list = lhSUserService.getAll(lhSUser, 1, 10);
				List<LhSUserEntity> lhSUserList = list.getResults();
				if(lhSUserList.size()>0){
					lhSUser=lhSUserList.get(0);
	//				Map<String,Object> attributes=new HashMap<String,Object>();
	//				attributes.put("status", lhSUser.getStatus());
	//				j.setAttributes(attributes);
					j.setObj(lhSUser);
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
