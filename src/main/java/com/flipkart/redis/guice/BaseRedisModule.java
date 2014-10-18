package com.flipkart.redis.guice;

import com.flipkart.redis.RedisClient;
import com.flipkart.redis.guice.providers.JedisProvider;
import com.google.inject.AbstractModule;
import com.google.inject.servlet.RequestScoped;

/**
 * Created by saurabh.agrawal on 18/10/14.
 */
public class BaseRedisModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RedisClient.class).toProvider(JedisProvider.class).in(RequestScoped.class);
    }
}
