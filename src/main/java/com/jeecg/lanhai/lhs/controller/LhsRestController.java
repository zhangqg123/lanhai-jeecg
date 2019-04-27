package com.jeecg.lanhai.lhs.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
import org.springframework.web.bind.annotation.RequestMethod;
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
@RequestMapping("/lhs/main")
@Api(value = "主程序rest服务", description = "主程序rest服务接口", tags = "RestMainAPI")
public class LhsRestController extends BaseController {
	@Autowired
	private ApiMainExamService apiMainExamService;

	@ApiOperation(value = "上传formid", produces = "application/json", httpMethod = "GET")
	@RequestMapping(value= "/uploadFormIds",method = RequestMethod.GET)
	public @ResponseBody AjaxJson uploadFormIds(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		String openId = request.getParameter("openId");
		String formIds = request.getParameter("formIds");
//		JSONArray formIdsArray = JSONArray.parseArray(formIds);
		List<FormTemplateVO> list = JSONObject.parseArray(formIds, FormTemplateVO.class);
		apiMainExamService.collect(openId, list);
		return j;
	}
	
}
