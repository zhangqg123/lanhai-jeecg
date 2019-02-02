package org.jeecgframework.web.api.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.jeecgframework.web.api.entity.FormTemplateVO;
import org.jeecgframework.web.api.service.ApiMainExamService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service("apiMainExamService")
public class ApiMainExamServiceImpl implements ApiMainExamService {
	@Resource
	private RedisTemplate<String,FormTemplateVO> redisTemplate;
	public void collect(String openId, List<FormTemplateVO> formTemplates) {
	    redisTemplate.opsForList().rightPushAll("mina:openid:" + openId, formTemplates);
//	    getValidFormId(openId);
	}
	
	public String getValidFormId(String openId) {
	    // 移除本次使用的和已过期的
	    List<FormTemplateVO> formTemplates = redisTemplate.opsForList().range("mina:openid:" + openId, 0, -1);

	    String validFormId = "";
	    int trimStart = 0;
	    int size=formTemplates.size();
	    for (int i = 0; i < size; i++) {
	    	FormTemplateVO formTemplate = (FormTemplateVO) formTemplates.get(i);
	    	long expireTime = formTemplate.getExpireTime();
	    	long date = System.currentTimeMillis();
	        if (expireTime > date) {
	            validFormId = formTemplate.getFormId();
	            trimStart = i + 1;
	            break;
	        }
	    }

	    // 移除本次使用的和已过期的
	    redisTemplate.opsForList().trim("mina:openid:" + openId, trimStart == 0 ? size : trimStart, -1);
	    return validFormId;
	}

}
