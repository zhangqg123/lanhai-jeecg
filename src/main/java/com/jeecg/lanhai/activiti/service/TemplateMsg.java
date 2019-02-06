package com.jeecg.lanhai.activiti.service;

import java.util.Map;

import com.jeecg.lanhai.activiti.util.WXTemplateData;



public interface TemplateMsg {
//	public String getRequestUrl();
	
	public boolean sendTemplateMsg(String requestUrl, String openId, String templateId, String page, String formId,
            Map<String, WXTemplateData> data, String emphasisKeyword);
}
