package com.jsj.cache;

import com.jsj.entity.Product;
import com.jsj.entity.Record;
import com.jsj.mapper.PanicBuyingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class RedisCachePool {


    @Autowired
    private JedisPool jedisPool;

    @Resource
    private PanicBuyingMapper panicBuyingMapper;

    public Jedis getJedis() {
        return jedisPool.getResource();
    }


    @PostConstruct
    public void loadData() {
        Jedis jedis = getJedis();
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