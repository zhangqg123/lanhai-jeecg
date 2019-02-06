package com.jeecg.lanhai.api.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.jeecg.lanhai.api.entity.FormTemplateVO;


public interface ApiMainExamService {
	public void collect(String openId, List<FormTemplateVO> formTemplates);
	public String getValidFormId(String openId);
	public JSONArray reply(String param, String openId, String examId, String appId);
}
