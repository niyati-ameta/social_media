package com.intuit.socialmedia.posts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisService {

    public int MAX_LIST_SIZE = 100;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Method to get a value from Redis
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Method to set a value in Redis
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void addToListAndTrim(String listKey, String newValue) {
        // Perform LPUSH operation to add new element at the head of the list
        redisTemplate.opsForList().leftPush(listKey, newValue);

        // Perform LTRIM operation to ensure list size does not exceed 100 elements
        redisTemplate.opsForList().trim(listKey, 0, MAX_LIST_SIZE - 1);
    }

    public List<Object> getList(String listKey) {
        return redisTemplate.opsForList().range(listKey, 0, -1);
    }

    public List<Object> multiGet(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    public void addList(String postPerUserKey, List<Object> value) {
        redisTemplate.opsForList().leftPushAll(postPerUserKey, value);
    }

    public void addOrUpdateMap(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public Object getFromMap(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }
}

