package com.ecommerce.project.service;

import com.ecommerce.project.model.Cart;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> T get(String key, Class<T> entityClass){
        try{
            Object o = redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper();
            if(o == null){
                return null;
            }
            return mapper.readValue(o.toString(), entityClass);
        }
        catch (Exception e) {
            log.error("Exception in redis service get method for " + entityClass, e);
            return null;
        }
    }
    public void set(String key, Object o, Long timeToLive){
        try{
            ObjectMapper mapper = new ObjectMapper();
            String obj = mapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, obj, timeToLive, TimeUnit.SECONDS);
        }
        catch (Exception e) {
            log.error("Exception in redis service set method for key " + key, e);
        }
    }
}
