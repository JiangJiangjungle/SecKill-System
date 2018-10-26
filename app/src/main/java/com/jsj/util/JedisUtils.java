package com.jsj.util;


import redis.clients.jedis.Jedis;

/**
 * 缓存工具类
 */
public interface JedisUtils {
    /**
     * 返回一个jedis
     * @return
     */
    Jedis getJedis();

    /**
     * 释放连接
     * @param jedis
     */
    void release(Jedis jedis);
}
