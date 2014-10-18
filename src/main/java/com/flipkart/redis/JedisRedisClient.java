package com.flipkart.redis;

import lombok.AllArgsConstructor;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

/**
 * Created by saurabh.agrawal on 16/10/14.
 */
@AllArgsConstructor
public class JedisRedisClient implements RedisClient {

    private final Jedis jedis;

    @Override
    public Long incr(String key) {
        return jedis.incr(key);
    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        return jedis.hmset(key, hash);
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        return jedis.hmget(key, fields);
    }
}
