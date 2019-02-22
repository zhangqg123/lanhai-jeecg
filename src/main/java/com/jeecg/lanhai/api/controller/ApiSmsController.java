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
import com.jeecg.lhs.service.LhSUserService;

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
	
	
	
	
	
		
}
