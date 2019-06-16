package com.jsj.mq.util.impl;

import com.jsj.api.util.CacheUtil;
import com.jsj.mq.util.JedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.Alias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@Component
@Alias("JedisUtils")
public class JedisUtilsImpl implements CacheUtil<RedisProperties.Jedis> {


    @Autowired
    private JedisPool jedisPool;

    @Override
    public RedisProperties.Jedis getResource() {
        return jedisPool.getR;
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    @Override
    public void release(Jedis jedis) {
        jedis.close();
    }
}
