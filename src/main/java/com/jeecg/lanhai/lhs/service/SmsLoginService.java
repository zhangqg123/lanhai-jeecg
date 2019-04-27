package com.jeecg.lanhai.lhs.service;

public interface SmsLoginService {

	String sendSms(String appId, String phone, String usertype, String status);
}
