package org.jeecgframework.web.api.service;

public interface SmsLoginService {

	String sendSms(String appId, String phone, String usertype);
}
