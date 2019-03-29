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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Alias("JedisUtils")
public class JedisUtilsImpl implements JedisUtils {
    @Autowired
    private JedisPool jedisPool;

    @Override
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    @Override
    public void release(Jedis jedis) {
        jedis.close();
    }
}
