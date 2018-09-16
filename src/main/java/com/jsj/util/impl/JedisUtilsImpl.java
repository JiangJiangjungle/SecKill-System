package com.jsj.util.impl;

import com.jsj.dao.RecordPoMapper;
import com.jsj.entity.RecordPO;
import com.jsj.exception.DAOException;
import com.jsj.util.JedisUtils;
import org.apache.ibatis.type.Alias;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component
@Alias("JedisUtils")
public class JedisUtilsImpl implements JedisUtils {

    @Resource
    private JedisPool jedisPool;

    @Resource
    private RecordPoMapper recordPoMapper;

    /**
     * 初始化数据
     */
    @PostConstruct
    public void loadData() throws Exception {
//        int MAX = 1000;
//        int count = 0;
//        List<RecordPO> recordPOList;
//        try {
//            do {
//                recordPOList = recordPoMapper.getAllRecords(count, count + MAX);
//                if (recordPOList == null) {
//                    break;
//                }
//                Jedis jedis = getJedis();
//                recordPOList.forEach((RecordPO recordPO) -> jedis.sadd(recordPO.getProductId(), recordPO.getUserId()));
//                count += MAX;
//            } while (recordPOList.size() == MAX);
//        } catch (DAOException d) {
//            throw new DAOException("加载交易记录数据时失败");
//        }
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
