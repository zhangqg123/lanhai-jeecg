package com.jeecg.lanhai.lhs.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.jeecg.ask.entity.LhDsAskEntity;
import com.jeecg.lanhai.lhs.entity.FormTemplateVO;

public interface LhsService {
	public void collect(String openId, List<FormTemplateVO> formTemplates);
	public String getValidFormId(String openId);
	public JSONArray reply(String param, String openId, String examId, String appId, String useTime);
	public void sendWeChat(String openId, String xcxId, String sendType);
}
