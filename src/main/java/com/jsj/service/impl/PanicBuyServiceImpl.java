package com.jsj.service.impl;

import com.jsj.lock.impl.RedisLock;
import com.jsj.lock.impl.ZookeeperLock;
import com.jsj.pojo.entity.ProductDO;
import com.jsj.exception.DAOException;
import com.jsj.exception.ServiceException;
import com.jsj.service.PanicBuyService;
import com.jsj.service.RecordService;
import com.jsj.util.JedisUtils;
import com.jsj.constant.BuyResultEnum;
import com.jsj.dao.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 秒杀抢购service
 *
 * @author jsj
 * @date 2018-9-22
 */
@Slf4j
@Service
@Alias("panicBuyService")
public class PanicBuyServiceImpl implements PanicBuyService {

    @Resource
    private RecordService recordService;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private JedisUtils jedisUtils;
    @Value("${data.hash-key}")
    private String hashKey;
    @Value("${data.redis-lock-key}")
    private String redisLockKey;

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
            boolean hasUser = jedis.sismember(productId, userId);
            if (hasUser) {
                // 返回重复秒杀信息
                return BuyResultEnum.REPEAT;
            }
            // 查询缓存中的库存数量
            String stockString = jedis.hget(hashKey, productId);
            int stock = StringUtils.isEmpty(stockString) ? 0 : Integer.parseInt(stockString);
            boolean finished = false;
            if (stock > 0) {
                // 乐观锁更新数据库中的库存数量
                Integer versionId = productMapper.getVersionId(productId);
                finished = productMapper.updateStockByLock(productId, versionId);
                // 若更新成功，则同时更新缓存并异步发送消息
                if (finished) {
                    this.updateAfterSuccess(userId, productId, stock, jedis);
                }
            }
            return finished ? BuyResultEnum.SUCCESS : BuyResultEnum.FAIL;
        } catch (DAOException d) {
            throw new ServiceException("DAOException导致");
        } finally {
            jedisUtils.release(jedis);
        }
    }

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public BuyResultEnum handleByPessimisticLock(String userId, String productId, int buyNumber) throws ServiceException {
        return null;
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
            boolean hasUser = jedis.sismember(productId, userId);
            if (hasUser) {
                // 返回重复秒杀信息
                return BuyResultEnum.REPEAT;
            }
            // 查询缓存中的库存数量
            String stockString = jedis.hget(hashKey, productId);
            int stock = StringUtils.isEmpty(stockString) ? 0 : Integer.parseInt(stockString);
            boolean finished = false;
            if (stock > 0) {
                String threadID = UUID.randomUUID().toString();
                // 加redis分布锁
                RedisLock redisLock = new RedisLock(redisLockKey, threadID, jedisUtils);
                boolean locked = redisLock.tryLock();
                if (locked) {
                    // 数据库普通更新库存-1
                    finished = productMapper.updateStock(productId);
                }
                redisLock.unlock();
                // 若更新成功，则同时更新缓存并异步发送消息
                if (finished) {
                    this.updateAfterSuccess(userId, productId, stock, jedis);
                }
            }
            return finished ? BuyResultEnum.SUCCESS : BuyResultEnum.FAIL;
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
            boolean hasUser = jedis.sismember(productId, userId);
            if (hasUser) {
                // 返回重复秒杀信息
                return BuyResultEnum.REPEAT;
            }
            // 查询缓存中的库存数量
            String stockString = jedis.hget(hashKey, productId);
            int stock = StringUtils.isEmpty(stockString) ? 0 : Integer.parseInt(stockString);
            boolean finished = false;
            if (stock > 0) {
                // todo 获取zookeeper分布锁并更新库存
                ZookeeperLock zookeeperLock = new ZookeeperLock();
                boolean locked = zookeeperLock.tryLock();
                if (locked) {
                    // 数据库普通更新库存-1
                    finished = productMapper.updateStock(productId);
                }
                // 若更新成功，则同时更新缓存并异步发送消息
                if (finished) {
                    this.updateAfterSuccess(userId, productId, stock, jedis);
                }
            }
            return finished ? BuyResultEnum.SUCCESS : BuyResultEnum.FAIL;
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
     * @author jsj
     * @date 2018-9-22
     */
    private void updateAfterSuccess(String userId, String productId, int stock, Jedis jedis) throws ServiceException {
        if (stock <= 0) {
            return;
        }
        // 更新缓存中的库存数量
        int nowStock = stock - 1;
        jedis.hset(hashKey, productId, String.valueOf(nowStock));
        log.info("商品：" + productId + " ，成功更新缓存中的库存数量:" + nowStock);
        // 添加到成功抢购名单
        jedis.sadd(productId, userId);
        log.info("商品：" + productId + " ，用户：" + userId + " ，添加到成功抢购名单");
        // 发送交易记录到消息队列
        recordService.sendRecordToMessageQueue(userId, productId, BuyResultEnum.SUCCESS.getValue());
        log.info("商品：" + productId + " ，用户：" + userId + " ，发送交易记录到消息队列");
    }
}