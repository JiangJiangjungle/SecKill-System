package com.jsj.cache;

import com.jsj.bean.Product;
import com.jsj.bean.Record;
import com.jsj.mapper.PanicBuyingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class RedisDataLoader {


    @Resource
    private PanicBuyingMapper panicBuyingMapper;

    @Resource
    private RedisCachePool redisCachePool;

    @PostConstruct
    public void initRedis() {
        Jedis jedis = redisCachePool.getJedis();
        jedis.flushAll();
        List<Product> products = panicBuyingMapper.getAllProducts();
        int id;
        int stock;
        for (Product product : products) {
            id = product.getProductId();
            stock = product.getStock();
            jedis.set(id + "_stock", stock + "");
        }
        List<Record> records = panicBuyingMapper.getAllRecords();
        for (Record record : records) {
            jedis.sadd(record.getProductId() + "_isBought", record.getUserId() + "");
        }
    }
}
