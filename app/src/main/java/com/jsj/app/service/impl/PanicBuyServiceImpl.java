package com.jsj.app.service.impl;

import com.jsj.app.config.RedisConfig;
import com.jsj.app.config.ZooKeeperConfig;
import com.jsj.app.constant.BuyResultEnum;
import com.jsj.app.dao.ProductMapper;
import com.jsj.app.exception.DAOException;
import com.jsj.app.exception.ServiceException;
import com.jsj.app.lock.impl.RedisLock;
import com.jsj.app.lock.impl.ZookeeperLock;
import com.jsj.app.pojo.entity.ProductDO;
import com.jsj.app.service.PanicBuyService;
import com.jsj.app.service.RecordService;
import com.jsj.app.util.JedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

/**
 * 秒杀抢购service
 *
 * @author com.jsj
 * @date 2018-9-22
 */
@Slf4j
@Service
@Alias("panicBuyService")
public class PanicBuyServiceImpl implements PanicBuyService {

    @Resource
    private JedisUtils jedisUtils;

    @Resource
    private RedisConfig redisConfig;
    @Resource
    private ZooKeeperConfig zooKeeperConfig;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private RecordService recordService;

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public BuyResultEnum handleByOptimisticLock(String userId, String productId, int buyNumber) throws ServiceException {
        if (StringUtils.isEmpty(userId)) {
            throw new ServiceException("userId不能为空");
        }
        if (StringUtils.isEmpty(productId)) {
            throw new ServiceException("productId不能为空");
        }
        log.info("商品：" + productId + " ，用户：" + userId + " ，调用handleByOptimisticLock方法");
        Jedis jedis = null;
        try {
            // 检查成功抢购名单中是否包含该用户
            jedis = jedisUtils.getJedis();
            if (jedis.sismember(productId, userId)) {
                // 返回重复秒杀信息
                return BuyResultEnum.REPEAT;
            }
            // 查询缓存中的库存数量
            String stockString = jedis.hget(redisConfig.getStockHashKey(), productId);
            int stock = StringUtils.isEmpty(stockString) ? 0 : Integer.parseInt(stockString);
            boolean updated = false;
            if (stock > 0) {
                // 乐观锁更新数据库中的库存数量
                Integer versionId = productMapper.getVersionId(productId);
                updated = productMapper.updateStockByLock(productId, versionId);
                // 若更新成功，获取最新的stock
                if (updated){
                    stock = productMapper.getStockById(productId);
                }
            }

            // 若更新成功，则同时更新缓存并异步发送消息
            if (updated) {
                this.updateAfterSuccess(userId, productId, stock, jedis);
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

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public BuyResultEnum handleByRedisLock(String userId, String productId, int buyNumber) throws ServiceException {
        if (StringUtils.isEmpty(userId)) {
            throw new ServiceException("userId不能为空");
        }
        if (StringUtils.isEmpty(productId)) {
            throw new ServiceException("productId不能为空");
        }
        log.info("商品：" + productId + " ，用户：" + userId + " ，调用handleByRedisLock方法");
        Jedis jedis = null;
        try {
            // 检查成功抢购名单中是否包含该用户
            jedis = jedisUtils.getJedis();
            if (jedis.sismember(productId, userId)) {
                // 返回重复秒杀信息
                return BuyResultEnum.REPEAT;
            }
            // 查询缓存中的库存数量
            String stockString = jedis.hget(redisConfig.getStockHashKey(), productId);
            int stock = StringUtils.isEmpty(stockString) ? 0 : Integer.parseInt(stockString);
            boolean updated = false;
            if (stock > 0) {
                String threadID = UUID.randomUUID().toString();
                // 加redis分布锁
                Lock redisLock = new RedisLock(redisConfig.getRedisLockKey(), threadID, jedisUtils);
                if (redisLock.tryLock()) {
                    // 数据库普通更新库存-1
                    updated = productMapper.updateStock(productId);
                    // 若更新成功，获取最新的stock
                    if (updated){
                        stock = productMapper.getStockById(productId);
                    }
                    redisLock.unlock();
                }
            }

            // 若更新成功，则同时更新缓存并异步发送消息
            if (updated) {
                this.updateAfterSuccess(userId, productId, stock, jedis);
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

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public BuyResultEnum handleByZookeeperLock(String userId, String productId, int buyNumber) throws ServiceException {
        if (StringUtils.isEmpty(userId)) {
            throw new ServiceException("userId不能为空");
        }
        if (StringUtils.isEmpty(productId)) {
            throw new ServiceException("productId不能为空");
        }
        log.info("商品：" + productId + " ，用户：" + userId + " ，调用handleByZookeeperLock方法");
        Jedis jedis = null;
        try {
            // 检查成功抢购名单中是否包含该用户
            jedis = jedisUtils.getJedis();
            if (jedis.sismember(productId, userId)) {
                // 返回重复秒杀信息
                return BuyResultEnum.REPEAT;
            }
            // 查询缓存中的库存数量
            String stockString = jedis.hget(redisConfig.getStockHashKey(), productId);
            int stock = StringUtils.isEmpty(stockString) ? 0 : Integer.parseInt(stockString);
            boolean updated = false;
            if (stock > 0) {
                // 获取zookeeper分布锁并更新库存
                Lock zookeeperLock = new ZookeeperLock(zooKeeperConfig.getHost(), zooKeeperConfig.getTimeout(),
                        zooKeeperConfig.getLockNameSpace(), zooKeeperConfig.getLockKey());
                boolean locked = zookeeperLock.tryLock();
                if (locked) {
                    // 若加锁之后数据库stock>0,则数据库普通更新库存-1
                    updated = productMapper.updateStock(productId);
                    // 若更新成功，获取最新的stock
                    if (updated){
                        stock = productMapper.getStockById(productId);
                    }
                    zookeeperLock.unlock();
                }
            }

            // 若更新成功，则同时更新缓存并异步发送消息
            if (updated) {
                this.updateAfterSuccess(userId, productId, stock, jedis);
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
    public int searchStock(String productId) throws ServiceException {
        if (StringUtils.isEmpty(productId)) {
            throw new ServiceException("id不能为空");
        }
        try {
            ProductDO productDO = productMapper.getProductById(productId);
            return productDO.getStock();
        } catch (DAOException d) {
            log.info("获取商品信息失败,商品id:" + productId);
            throw new ServiceException("获取商品信息失败");
        }
    }

    /**
     * 数据库更新库存后，执行缓存和消息队列操作
     *
     * @param userId
     * @param productId
     * @param stock
     * @throws ServiceException
     * @author com.jsj
     * @date 2018-9-22
     */
    private void updateAfterSuccess(String userId, String productId, int stock, Jedis jedis) throws ServiceException {
        if (stock <= 0) {
            return;
        }
        // 更新缓存中的库存数量
        int nowStock = stock - 1;
        jedis.hset(redisConfig.getStockHashKey(), productId, String.valueOf(nowStock));
        log.info("商品：" + productId + " ，成功更新缓存中的库存数量:" + nowStock);
        // 添加到成功抢购名单
        jedis.sadd(productId, userId);
        log.info("商品：" + productId + " ，用户：" + userId + " ，添加到成功抢购名单");
        // 发送交易记录到消息队列
        recordService.sendRecordToMessageQueue(userId, productId, BuyResultEnum.SUCCESS.getValue());
        log.info("商品：" + productId + " ，用户：" + userId + " ，发送交易记录到消息队列");
    }
}