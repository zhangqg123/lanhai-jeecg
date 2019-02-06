package com.jeecg.lanhai.api.service;

public interface SmsLoginService {

	String sendSms(String appId, String phone, String usertype, String status);
}
