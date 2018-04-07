package com.jsj;

import com.jsj.cache.RedisCachePool;
import com.jsj.cache.RedisDataLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheTest {

    @Resource
    private RedisCachePool redisCachePool;

    @Test
    public void test() {
        Jedis jedis1 = redisCachePool.getJedis();
        String key = "iphone_stock";
        jedis1.watch(key);
        Jedis jedis2 = redisCachePool.getJedis();
        jedis2.decr(key);
        Transaction tx = jedis1.multi();
        tx.decr(key);
        List<Object> result = tx.exec();

        System.out.println(result.isEmpty());
    }

}
