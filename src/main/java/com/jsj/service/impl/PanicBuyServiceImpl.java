package com.jsj.service.impl;

import com.jsj.pojo.entity.ProductPO;
import com.jsj.exception.DAOException;
import com.jsj.exception.ServiceException;
import com.jsj.service.PanicBuyService;
import com.jsj.service.RecordService;
import com.jsj.util.JedisUtils;
import com.jsj.pojo.ServiceResult;
import com.jsj.dao.ProductPoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

@Slf4j
@Service
@Alias("panicBuyService")
public class PanicBuyServiceImpl implements PanicBuyService {

    @Resource
    private RecordService recordService;

    @Resource
    private ProductPoMapper productPoMapper;

    @Resource
    private JedisUtils jedisUtils;

    @Value("${data.table-key}")
    private String TABLE_KEY;

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public ServiceResult handleByOptimisticLock(String userId, String productId, int buyNumber) throws ServiceException {
        if (StringUtils.isEmpty(userId)) {
            throw new ServiceException("userId不能为空");
        }
        if (StringUtils.isEmpty(productId)) {
            throw new ServiceException("productId不能为空");
        }
        log.info("调用handleByOptimisticLock方法：userId:" + userId + ",productId:" + productId);
        Jedis jedis = null;
        try {
            //检查成功抢购名单中是否包含该用户
            jedis = jedisUtils.getJedis();
            boolean hasUser = jedis.sismember(productId, userId);
            if (hasUser) {
                //返回重复秒杀信息
                return ServiceResult.REPEAT;
            }
            //查询数据库中的库存数量
            Integer stock = Integer.parseInt(jedis.hget(TABLE_KEY, productId));
            boolean finished = false;
            if (stock > 0) {
                //乐观锁更新数据库中的库存数量
                Integer versionId = productPoMapper.getVersionId(productId);
                finished = productPoMapper.updateProductStock(productId, versionId);
                //若更新成功，则更新缓存并异步发送消息
                if (finished) {
                    //更新缓存中的库存数量
                    jedis.hset(TABLE_KEY, productId, String.valueOf(stock - 1));
                    //添加到成功抢购名单
                    jedis.sadd(productId, userId);
                    //发送交易记录到消息队列
                    recordService.sendRecordToMessageQueue(userId, productId, ServiceResult.SUCCESS.getValue());
                }
            }
            return finished ? ServiceResult.SUCCESS : ServiceResult.FAIL;
        } catch (DAOException d) {
            throw new ServiceException("DAOException导致");
        } finally {
            jedisUtils.release(jedis);
        }
    }

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public ServiceResult handleByPessimisticLock(String userId, String productId, int buyNumber) throws ServiceException {
        return null;
    }

    @Override
    public int searchStock(String productId) throws ServiceException {
        if (StringUtils.isEmpty(productId)) {
            throw new ServiceException("id不能为空");
        }
        try {
            ProductPO productPO = productPoMapper.getProductById(productId);
            return productPO.getStock();
        } catch (DAOException d) {
            log.info("获取商品信息失败,商品id:" + productId);
            throw new ServiceException("获取商品信息失败");
        }
    }
}