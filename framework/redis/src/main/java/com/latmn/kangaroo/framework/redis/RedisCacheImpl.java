package com.latmn.kangaroo.framework.redis;

import com.latmn.kangaroo.framework.core.cache.ICache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisCacheImpl implements ICache {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void put(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void put(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Object getAndDelete(String key) {
        return redisTemplate.opsForValue().getAndDelete(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
