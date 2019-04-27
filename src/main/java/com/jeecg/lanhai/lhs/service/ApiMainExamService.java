package com.jeecg.lanhai.lhs.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.jeecg.lanhai.lhs.entity.FormTemplateVO;

public interface ApiMainExamService {
	public void collect(String openId, List<FormTemplateVO> formTemplates);
	public String getValidFormId(String openId);
	public JSONArray reply(String param, String openId, String examId, String appId, String useTime);
}
