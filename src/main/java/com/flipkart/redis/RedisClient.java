package com.flipkart.redis;

import java.util.Map;

/**
 * Created by saurabh.agrawal on 16/10/14.
 */
public interface RedisClient {
    public Long incr(String key);

    public String hmset(String key, Map<String,String> hash);

    public java.util.List<String> hmget(String key, String... fields);

    public Long expire(String key, int seconds);
}
