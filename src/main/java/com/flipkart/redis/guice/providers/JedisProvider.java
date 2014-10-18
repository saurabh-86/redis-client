package com.flipkart.redis.guice.providers;

import com.flipkart.redis.JedisRedisClient;
import com.flipkart.redis.configuration.RedisConfiguration;
import com.flipkart.redis.RedisClient;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import redis.clients.jedis.JedisPool;

/**
 * Created by saurabh.agrawal on 16/10/14.
 */
@Singleton
public class JedisProvider implements Provider<RedisClient> {

    private final RedisConfiguration configuration;

    private final JedisPool pool;

    @Inject
    public JedisProvider(RedisConfiguration config) {
        this.configuration = config;
        this.pool = new JedisPool(config.getRedisHost(), config.getRedisPort());
    }

    @Override
    public RedisClient get() {
        return new JedisRedisClient(pool.getResource());
    }
}
