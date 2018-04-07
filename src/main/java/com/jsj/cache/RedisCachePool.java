package com.jsj.cache;

import com.jsj.config.RedisConfig;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class RedisCachePool {

    @Resource
    private RedisConfig redisConfig;

    private JedisPool jedisPool;

    @PostConstruct
    private void initPool() {
        JedisPoolConfig poolConfig = redisConfig.initJedisPoolConfig();
        String host = redisConfig.getHost();
        int port = redisConfig.getPort();
        int timeout = redisConfig.getTimeout();
        jedisPool = new JedisPool(poolConfig, host, port, timeout);
    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }
}