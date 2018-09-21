package com.jsj.util.impl;

import com.jsj.dao.ProductPoMapper;
import com.jsj.dao.RecordPoMapper;
import com.jsj.entity.ProductPO;
import com.jsj.entity.RecordPO;
import com.jsj.exception.DAOException;
import com.jsj.util.JedisUtils;
import org.apache.ibatis.type.Alias;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Alias("JedisUtils")
public class JedisUtilsImpl implements JedisUtils {
    @Resource
    private JedisPool jedisPool;

    @Resource
    private RecordPoMapper recordPoMapper;
    @Resource
    private ProductPoMapper productPoMapper;

    @Value("${data.table-key}")
    private String TABLE_KEY;

    /**
     * 初始化数据
     */
    @PostConstruct
    public void loadData() throws Exception {
        int count = 0;
        Jedis jedis = getJedis();
        List<RecordPO> recordPOList;
        List<ProductPO> productPOList;
        /**
         * 分页查询限制
         */
        int LIMIT_MAX = 1000;
        try {
            //加载交易记录
            do {
                recordPOList = recordPoMapper.getAllRecords(count, count + LIMIT_MAX);
                if (recordPOList == null || recordPOList.isEmpty()) {
                    break;
                }
                recordPOList.forEach((RecordPO recordPO) -> jedis.sadd(recordPO.getProductId(), recordPO.getUserId()));
                count += LIMIT_MAX;
            } while (recordPOList.size() == LIMIT_MAX);
            count = 0;
            //加载库存记录
            do {
                productPOList = productPoMapper.getAllStock(count, count + LIMIT_MAX);
                if (productPOList == null || productPOList.isEmpty()) {
                    break;
                }
                Map<String, String> stocksMap = new HashMap<>(productPOList.size());
                productPOList.forEach((ProductPO product) -> stocksMap.put(product.getId(), String.valueOf(product.getStock())));
                jedis.hmset(TABLE_KEY, stocksMap);
                count += LIMIT_MAX;
            } while (productPOList.size() == LIMIT_MAX);
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
