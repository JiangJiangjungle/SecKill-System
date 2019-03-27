package com.jsj.app.service.impl;

import com.jsj.app.config.RedisConfig;
import com.jsj.app.constant.BuyResultEnum;
import com.jsj.app.dao.ProductMapper;
import com.jsj.app.dao.RecordMapper;
import com.jsj.app.exception.DAOException;
import com.jsj.app.exception.ServiceException;
import com.jsj.app.pojo.entity.RecordDO;
import com.jsj.app.service.TestService;
import com.jsj.app.util.JedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.Date;

@Slf4j
@Service
public class TestServiceImpl implements TestService, ApplicationContextAware {

    @Autowired
    private JedisUtils jedisUtils;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private RedisConfig redisConfig;

    /**
     * 自己的代理对象
     */
    private TestService proxy;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.proxy = applicationContext.getBean(TestService.class);
    }

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public BuyResultEnum handleWithoutRedis(String userId, String productId, int buyNumber) throws ServiceException {
        try {
            boolean updated = this.proxy.updateAndInsertRecord(userId, productId, true);
            // 若更新成功，则同时更新缓存并异步发送消息
            if (updated) {
                return BuyResultEnum.SUCCESS;
            } else {
                log.info("库存不足，秒杀失败..userId: " + userId);
                return BuyResultEnum.FAIL;
            }
        } catch (DAOException d) {
            throw new ServiceException("DAOException导致");
        }
    }

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public BuyResultEnum handleWithoutRedis2(String userId, String productId, int buyNumber) throws ServiceException {
        try {
            boolean updated = this.proxy.updateAndInsertRecord(userId, productId, false);
            // 若更新成功，则同时更新缓存并异步发送消息
            if (updated) {
                return BuyResultEnum.SUCCESS;
            } else {
                log.info("库存不足，秒杀失败..userId: " + userId);
                return BuyResultEnum.FAIL;
            }
        } catch (DAOException d) {
            throw new ServiceException("DAOException导致");
        }
    }

    @Override
    public BuyResultEnum handleByOptimisticLockAndRedisWithOutTranscation(String userId, String productId, int buyNumber) throws ServiceException {
        Jedis jedis = null;
        try {
            // 检查成功抢购名单中是否包含该用户
            jedis = jedisUtils.getJedis();
            if (jedis.sismember(productId, userId)) {
                // 返回重复秒杀信息
                return BuyResultEnum.REPEAT;
            }
            // 查询缓存中的库存数量
            int stock = chechStockByRedis(productId, jedis);
            boolean updated = false;
            if (stock > 0) {
                // 乐观锁更新数据库中的库存数量并新增交易记录
                updated = this.proxy.updateAndInsertRecord(userId, productId, true);
            }
            // 若更新成功，则同时更新缓存
            if (updated) {
                log.info("商品id：{} ,用户id：{} 秒杀成功,数据库库存更新：{}", productId, userId, stock);
                this.updateUserList(userId, productId, jedis);
                return BuyResultEnum.SUCCESS;
            }
            log.info("乐观加锁失败或库存不足，秒杀失败..userId: " + userId);
            return BuyResultEnum.FAIL;
        } catch (DAOException d) {
            throw new ServiceException("DAOException导致");
        } finally {
            jedisUtils.release(jedis);
        }
    }

    @Override
    public BuyResultEnum handleByPessmisticLockAndRedisWithOutTranscation(String userId, String productId, int buyNumber) throws ServiceException {
        Jedis jedis = null;
        try {
            // 检查成功抢购名单中是否包含该用户
            jedis = jedisUtils.getJedis();
            if (jedis.sismember(productId, userId)) {
                // 返回重复秒杀信息
                return BuyResultEnum.REPEAT;
            }
            // 查询缓存中的库存数量
            int stock = chechStockByRedis(productId, jedis);
            boolean updated = false;
            if (stock > 0) {
                // 乐观锁更新数据库中的库存数量并新增交易记录
                updated = this.proxy.updateAndInsertRecord(userId, productId, false);
            }
            // 若更新成功，则同时更新抢购名单
            if (updated) {
                this.updateUserList(userId, productId, jedis);
                return BuyResultEnum.SUCCESS;
            }
            log.info("库存不足，秒杀失败..userId: " + userId);
            return BuyResultEnum.FAIL;
        } catch (DAOException d) {
            throw new ServiceException("DAOException导致");
        } finally {
            jedisUtils.release(jedis);
        }
    }

    @Override
    @Transactional(rollbackFor = DAOException.class)
    public boolean updateAndInsertRecord(String userId, String productId, boolean optimisticLock) throws DAOException {
        boolean updated;
        if (optimisticLock) {
            Integer versionId = productMapper.getVersionId(productId);
            updated = productMapper.updateStockByLock(productId, versionId);
        } else {
            updated = productMapper.updateStock(productId);
        }
        if (updated) {
            RecordDO recordDO = new RecordDO(userId, productId, BuyResultEnum.SUCCESS.getValue(), new Date());
            recordMapper.addRecord(recordDO);
        }
        return updated;
    }


    @Transactional(rollbackFor = DAOException.class)
    public boolean updateAndSendMessage(String userId, String productId, boolean optimisticLock) throws DAOException {
        boolean updated;
        if (optimisticLock) {
            Integer versionId = productMapper.getVersionId(productId);
            updated = productMapper.updateStockByLock(productId, versionId);
        } else {
            updated = productMapper.updateStock(productId);
        }
        if (updated) {
            RecordDO recordDO = new RecordDO(userId, productId, BuyResultEnum.SUCCESS.getValue(), new Date());
            recordMapper.addRecord(recordDO);
        }
        return updated;
    }


    /**
     * 添加到成功抢购名单
     *
     * @param userId
     * @param productId
     * @param jedis
     * @throws ServiceException
     */
    private void updateUserList(String userId, String productId, Jedis jedis) {
        jedis.sadd(productId, userId);
        log.info("商品：" + productId + " ，用户：" + userId + " ，添加到成功抢购名单");
    }

    /**
     * 查询缓存中的库存数量
     *
     * @param productId
     * @param jedis
     * @return
     */
    private int chechStockByRedis(String productId, Jedis jedis) {
        String stockString = jedis.hget(redisConfig.getStockHashKey(), productId);
        return StringUtils.isEmpty(stockString) ? 0 : Integer.parseInt(stockString);
    }

}
