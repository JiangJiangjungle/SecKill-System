package com.jsj.service.util.impl;

import com.jsj.api.util.CacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@Component
public class JedisUtilImpl implements CacheUtil<Jedis> {
    @Autowired
    private JedisPool jedisPool;

    @Override
    public Jedis getResource() {
        return jedisPool.getResource();
    }

    @Override
    public void releaseResource(Jedis jedis) {
        jedis.close();
    }
}
