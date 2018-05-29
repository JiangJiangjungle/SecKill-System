package com.jsj;

import com.jsj.cache.RedisCachePool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheTest {

    @Resource
    private RedisCachePool redisCachePool;

    @Test
    public void test() {
        Jedis jedis1 = redisCachePool.getJedis();
        String key = "lalala";
        jedis1.set(key,"1");

    }
}
