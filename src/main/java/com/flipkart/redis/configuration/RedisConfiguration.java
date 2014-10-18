package com.flipkart.redis.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by saurabh.agrawal on 16/10/14.
 */
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class RedisConfiguration {
    private String redisHost;
    private int redisPort;
}
