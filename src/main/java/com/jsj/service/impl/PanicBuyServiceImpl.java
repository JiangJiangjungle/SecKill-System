package com.jsj.service.impl;

import com.jsj.entity.ProductPO;
import com.jsj.exception.DAOException;
import com.jsj.exception.ServiceException;
import com.jsj.service.PanicBuyService;
import com.jsj.service.RecordService;
import com.jsj.util.JedisUtils;
import com.jsj.util.ServiceResult;
import com.jsj.dao.ProductPoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;
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

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public ServiceResult handleByOptimisticLock(String userId, String productId, int buyNumber) throws ServiceException {
        return null;
    }

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public ServiceResult handleByPessimisticLock(String userId, String productId, int buyNumber) throws ServiceException {
        if (StringUtils.isEmpty(userId)) {
            throw new ServiceException("userId不能为空");
        }
        if (StringUtils.isEmpty(productId)) {
            throw new ServiceException("productId不能为空");
        }
        Jedis jedis = null;
        try {
            //检查成功抢购名单中是否包含该用户
            jedis = jedisUtils.getJedis();
            boolean hasUser = jedis.sismember(productId, userId);
            if (hasUser) {
                //返回重复秒杀信息
                return ServiceResult.REPEAT;
            }
            ProductPO productPO = productPoMapper.getProductById(productId);
            boolean finished = false;
            if (productPO.getStock()>0){
                finished = productPoMapper.updateProductStock(productId,productPO.getVersionId());
            }
            if (finished) {
                //添加到成功抢购名单
                jedis.sadd(productId, userId);
                //todo 发送交易记录到消息队列
                recordService.sendRecordToMessageUtil(userId, productId, ServiceResult.SUCCESS.getValue());
                return ServiceResult.SUCCESS;
            }
            return ServiceResult.FAIL;
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
            ProductPO productPO = productPoMapper.getProductById(productId);
            return productPO.getStock();
        } catch (DAOException d) {
            log.info("获取商品信息失败,商品id:" + productId);
            throw new ServiceException("获取商品信息失败");
        }
    }
}