package com.jsj.service;

import com.alibaba.fastjson.JSON;
import com.jsj.atomicstock.ProductAtomicStock;
import com.jsj.cache.RedisCachePool;
import com.jsj.constant.ServiceResult;
import com.jsj.entity.Record;
import com.jsj.mapper.PanicBuyingMapper;
import com.jsj.mq.RecordSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class PanicBuyingService {

    @Resource
    private RecordSender recordSender;

    @Resource
    private ProductAtomicStock productAtomicStock;

    @Resource
    private RedisCachePool redisCachePool;

    @Resource
    private PanicBuyingMapper panicBuyingMapper;

    /**
     * 利用原子类作为计数器实现抢购
     *
     * @param paramMap
     * @return
     */
    @Transactional
    public ServiceResult handleByAtomicStock(Map<String, Object> paramMap) {
        //获得用户ID
        Integer userId = (Integer) paramMap.get("userId");
        //获得商品ID
        Integer productId = (Integer) paramMap.get("productId");
        //获得redis连接
        Jedis jedis = redisCachePool.getJedis();
        //检查是否重复购买
        if (jedis.sismember(productId + "_isBought", userId + "")) return ServiceResult.REPEAT;
        //根据商品ID得到原子数量实例
        AtomicInteger stock = productAtomicStock.getStockById(productId);
        try {
            if (stock == null) {
                log.info("不存在商品ID为： " + productId + " 的商品");
                return ServiceResult.FAIL;
            }
            int now = stock.get();
            if (now > 0 && stock.compareAndSet(now, now - 1)) {
                Record record = new Record(userId, productId, ServiceResult.SUCCESS.getValue(), new Date());
                recordSender.sendMsg(JSON.toJSONString(record));
                jedis.sadd(productId + "_isBought", userId + "");
                return ServiceResult.SUCCESS;
            }
            return ServiceResult.FAIL;
        } finally {
            jedis.close();
        }
    }

    /**
     * 利用redis事务支持实现抢购
     *
     * @param paramMap
     * @return
     */
    @Transactional
    public ServiceResult handleByRedisStock(Map<String, Object> paramMap) {
        //获得用户ID
        Integer userId = (Integer) paramMap.get("userId");
        //获得商品ID
        Integer productId = (Integer) paramMap.get("productId");
        //获得redis连接
        Jedis jedis = redisCachePool.getJedis();
        //检查是否重复购买
        if (jedis.sismember(productId + "_isBought", userId + "")) return ServiceResult.REPEAT;
        String key = productId + "_stock";
        try {
            //开启监视
            jedis.watch(key);
            int stock = Integer.valueOf(jedis.get(key));
            if (stock > 0) {
                //开启事务
                Transaction tx = jedis.multi();
                tx.decr(key);
                //提交事务
                List<Object> result = tx.exec();
                if (!result.isEmpty()) {
                    Record record = new Record(userId, productId, ServiceResult.SUCCESS.getValue(), new Date());
                    recordSender.sendMsg(JSON.toJSONString(record));
                    jedis.sadd(productId + "_isBought", userId + "");
                    return ServiceResult.SUCCESS;
                }
            }
            return ServiceResult.FAIL;
        } finally {
            jedis.close();
        }
    }

    /**
     * 利用MySQL行锁进行更新
     *
     * @param paramMap
     * @return
     */
    @Transactional
    public ServiceResult handleByMySQLLock(Map<String, Object> paramMap) {
        //获得用户ID
        Integer userId = (Integer) paramMap.get("userId");
        //获得商品ID
        Integer productId = (Integer) paramMap.get("productId");
        //获得redis连接
        Jedis jedis = redisCachePool.getJedis();
        //检查是否重复购买
        if (jedis.sismember(productId + "_isBought", userId + "")) return ServiceResult.REPEAT;
        try {
            //执行减一操作
            if (panicBuyingMapper.decreaseProductStock(productId)) {
                Record record = new Record(userId, productId, ServiceResult.SUCCESS.getValue(), new Date());
                panicBuyingMapper.addRecord(record);
                jedis.sadd(productId + "_isBought", userId + "");
                return ServiceResult.SUCCESS;
            }
            return ServiceResult.FAIL;
        } finally {
            jedis.close();
        }
    }
}