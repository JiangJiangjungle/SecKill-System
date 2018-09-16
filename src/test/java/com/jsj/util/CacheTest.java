package com.jsj.util;

import com.jsj.util.RedisCachePool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheTest {

    @Resource
    JedisUtils jedisUtils;

    @Test
    public void test() {
        Jedis jedis = jedisUtils.getJedis();
        Set<String> keys= jedis.keys("*");
        keys.forEach(System.out::println);
    }
}
