package com.jsj.app.util.impl;

import com.jsj.app.config.RedisConfig;
import com.jsj.app.dao.ProductMapper;
import com.jsj.app.dao.RecordMapper;
import com.jsj.app.exception.DAOException;
import com.jsj.app.pojo.entity.ProductDO;
import com.jsj.app.pojo.entity.RecordDO;
import com.jsj.app.util.JedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.Alias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Alias("JedisUtils")
public class JedisUtilsImpl implements JedisUtils {
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private RedisConfig redisConfig;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private ProductMapper productMapper;


    /**
     * 初始化数据
     */
    @PostConstruct
    public void loadData() throws Exception {
        int count = 0;
        Jedis jedis = getJedis();
        //刷新缓存
        jedis.flushDB();
        log.info("刷新缓存");
        List<RecordDO> recordDOList;
        List<ProductDO> productDOList;
        Map<String, String> stocksMap;
        try {
            //加载交易记录
            do {
                recordDOList = recordMapper.getAllRecords(count, count + redisConfig.LIMIT_MAX);
                if (recordDOList == null || recordDOList.isEmpty()) {
                    break;
                }
                recordDOList.forEach((RecordDO recordDO) -> jedis.sadd(recordDO.getProductId(), recordDO.getUserId()));
                log.info("加载交易记录第" + count + "-" + (count + recordDOList.size()) + "项到redis缓存");
                count += redisConfig.LIMIT_MAX;
            } while (recordDOList.size() == redisConfig.LIMIT_MAX);

            //加载库存记录
            count = 0;
            do {
                productDOList = productMapper.getAllStock(count, count + redisConfig.LIMIT_MAX);
                if (productDOList == null || productDOList.isEmpty()) {
                    break;
                }
                stocksMap = new HashMap<>(productDOList.size());
                for (ProductDO productDO : productDOList) {
                    stocksMap.put(productDO.getId(), String.valueOf(productDO.getStock()));
                }
                jedis.hmset(redisConfig.getStockHashKey(), stocksMap);
                log.info("加载库存记录第" + count + "-" + (count + stocksMap.size()) + "项到redis缓存");
                count += redisConfig.LIMIT_MAX;
            } while (productDOList.size() == redisConfig.LIMIT_MAX);
        } catch (DAOException d) {
            throw new DAOException("加载交易和库存记录数据时失败");
        } finally {
            jedis.close();
        }
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
