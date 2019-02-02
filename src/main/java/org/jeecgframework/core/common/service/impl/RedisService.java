package org.jeecgframework.core.common.service.impl;


import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * RedisService工具类
 */
@Service
public class RedisService {

	@Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
    *  删除key和value
    */
    public void delete(String key){
        stringRedisTemplate.delete(key);
    }

    /**
    *  根据key获取value
    */
    public String get(String key){
        String value = stringRedisTemplate.opsForValue().get(key);
        return value;
    }

    /**
    *  将key和value存入redis，并设置有效时间，单位：天
    */
    public void set(String key, String value, long timeout){
        stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.DAYS);
    }

    /**
    *  将key和value存入redis
    */
    public void set(String key, String value){
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
    *  从redis中获取map
    */
    public Map<String, Object> getMap(String key){
        HashOperations<String, String, Object>  hash = stringRedisTemplate.opsForHash();
        Map<String,Object> map = hash.entries(key);
        return map;
    }

    /**
    *  将map存入redis，并设置时效
    */
    public void set(String key, Map<? extends String, ? extends Object> map, long timeout){
        stringRedisTemplate.opsForHash().putAll(key, map);
        stringRedisTemplate.expire(key, timeout, TimeUnit.DAYS);
    }
}
