package com.jeecg.lanhai.api.controller;


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
import com.jeecg.exam.service.LhExamService;
import com.jeecg.lanhai.api.entity.FormTemplateVO;
import com.jeecg.lanhai.api.service.ApiMainExamService;
import com.jeecg.zwzx.service.WorkUserService;

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
		String appId=request.getParameter("xcxId");
		JSONArray jsonArray =apiMainExamService.reply(param,openId,examId,appId);
		return JSONArray.toJSONString(jsonArray);
	}

}
