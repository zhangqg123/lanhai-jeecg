package com.jeecg.lanhai.lhs.controller;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.p3.core.common.utils.AjaxJson;
import org.jeecgframework.p3.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecg.lanhai.lhs.entity.FormTemplateVO;
import com.jeecg.lanhai.lhs.service.ApiMainExamService;


/**
 * CMS API
 * 
 * @author zhangdaihao
 * 
 */
@Controller
@RequestMapping("/api/main/exam")
public class ApiMainExamController extends BaseController {
	@Autowired
	private ApiMainExamService apiMainExamService;

	@RequestMapping("/uploadFormIds")
	public @ResponseBody AjaxJson uploadFormIds(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		String openId = request.getParameter("openId");
		String formIds = request.getParameter("formIds");
//		JSONArray formIdsArray = JSONArray.parseArray(formIds);
		List<FormTemplateVO> list = JSONObject.parseArray(formIds, FormTemplateVO.class);
		apiMainExamService.collect(openId, list);
		return j;
	}
	
	@RequestMapping(value="/subChoose")
	public @ResponseBody String subChoose(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String param = request.getParameter("param");
		String openId = request.getParameter("openId");
		String examId = request.getParameter("examId");
		String sTime=request.getParameter("startTime");
		long startTime=Long.valueOf(sTime);
		long endTime = System.currentTimeMillis();
		int time=(int) ((endTime-startTime)/1000);
		String useTime=null;
		int min=0;
		int sec=0;
		if (time>60){
			min=(int)time/60;
			sec=time%60;
			useTime=min+"分"+sec+"秒";
		}else{
			sec=time%60;
			useTime=sec+"秒";
		}
		String appId=request.getParameter("xcxId");
		JSONArray jsonArray =apiMainExamService.reply(param,openId,examId,appId,useTime);
		return JSONArray.toJSONString(jsonArray);
	}

}
