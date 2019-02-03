package org.jeecgframework.web.api.service;

import java.util.List;

import org.jeecgframework.web.api.entity.FormTemplateVO;

import com.alibaba.fastjson.JSONArray;


public interface ApiMainExamService {
	public void collect(String openId, List<FormTemplateVO> formTemplates);
	public String getValidFormId(String openId);
	public JSONArray reply(String param, String openId, String examId, String appId);
}
