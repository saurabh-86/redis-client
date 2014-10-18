package com.flipkart.redis;

/**
 * Created by saurabh.agrawal on 16/10/14.
 */
public interface RedisClient {
    public java.lang.Long incr(java.lang.String key);

    public java.lang.String hmset(java.lang.String key, java.util.Map<java.lang.String,java.lang.String> hash);

    public java.util.List<java.lang.String> hmget(java.lang.String key, java.lang.String... fields);

}
