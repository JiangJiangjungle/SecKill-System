package com.jsj.lock.impl;

import com.jsj.lock.AbstractLock;
import com.jsj.util.JedisUtils;
import lombok.Data;
import redis.clients.jedis.Jedis;

/**
 * @author jsj
 * @date 2018-9-22
 */
@Data
public class RedisLock extends AbstractLock {
    /**
     * 用于获取缓存连接
     */
    private JedisUtils jedisUtils;

    /**
     * 锁key值
     */
    private String lockKey;

    /**
     * 锁value值:即线程id
     */
    private String lockValue;

    /**
     * 是否持有锁
     */
    private volatile boolean locked = false;

    /**
     * 锁超时时间，防止线程在入锁以后，无限的执行等待，默认30000ms
     */
    private static final long EXPIRE_MILLISECOND = 30000;

    /**
     * 设置键的过期时间为 millisecond 毫秒
     */
    private static final String PX = "PX";

    /**
     * 只在键不存在时，才对键进行设置操作
     */
    private static final String NX = "NX";

    private static final String OK = "OK";

    public RedisLock(String lockKey, String lockValue, JedisUtils jedisUtils) {
        this.lockKey = lockKey;
        this.lockValue = lockValue;
        this.jedisUtils = jedisUtils;
    }

    @Override
    public boolean tryLock() {
        Jedis jedis = jedisUtils.getJedis();
        String result = jedis.set(lockKey, lockValue, NX, PX, EXPIRE_MILLISECOND);
        if (OK.equals(result)) {
            locked = true;
        }
        jedisUtils.release(jedis);
        return locked;
    }

    @Override
    public void unlock() {
        Jedis jedis = jedisUtils.getJedis();
        if (locked && this.lockValue.equals(jedis.get(lockKey))) {
            jedis.del(lockKey);
        }
        locked = false;
        jedisUtils.release(jedis);
    }
}
