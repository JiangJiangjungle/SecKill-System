package com.jsj.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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
//        Jedis jedis = getJedis();
//        jedis.flushAll();
//        List<ProductPO> productPOS = panicBuyingMapper.getAllProducts();
//        String id;
//        int stock;
//        for (ProductPO productPO : productPOS) {
//            id = productPO.getId();
//            stock = productPO.getStock();
//            jedis.set(id + "_stock", stock + "");
//        }
//        List<RecordPO> recordPOS = panicBuyingMapper.getAllRecords();
//        for (RecordPO recordPO : recordPOS) {
//            jedis.sadd(recordPO.getProductId() + "_isBought", recordPO.getUserId() + "");
//        }
    }
}