package com.jsj.service.service.impl;

import com.jsj.api.BuyResultEnum;
import com.jsj.api.entity.RecordDO;
import com.jsj.api.exception.DAOException;
import com.jsj.api.exception.ServiceException;
import com.jsj.api.service.SecKillService;
import com.jsj.api.util.CacheUtil;
import com.jsj.service.config.RedisConfig;
import com.jsj.service.dao.ProductMapper;
import com.jsj.service.dao.RecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.Date;

@Slf4j
@Service("secKillService")
public class SecKillServiceImpl implements SecKillService, ApplicationContextAware {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private CacheUtil<Jedis> jedisCacheUtil;
    /**
     * 该service的代理Bean
     */
    private SecKillService proxy;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.proxy = applicationContext.getBean(SecKillService.class);
    }

    @Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
    @Override
    public boolean decreaseStockAndAddRecord(Long userId, Long productId, Boolean optimisticLock) throws ServiceException {
        boolean updated;
        try {
            if (optimisticLock) {
                Integer versionId = productMapper.getVersionIdByPrimaryId(productId);
                updated = productMapper.decreaseStockByVersionId(productId, versionId);
            } else {
                updated = productMapper.decreaseStock(productId);
            }
            if (updated) {
                RecordDO recordDO = new RecordDO(userId, productId, BuyResultEnum.SUCCESS.getValue(), new Date());
                recordMapper.addRecord(recordDO);
            }
        } catch (DAOException d) {
            throw new ServiceException(d.getMessage());
        }
        return updated;
    }

    @Override
    public BuyResultEnum handle(Long userId, Long productId, Integer buyNumber, Boolean optimisticLock) throws ServiceException {
        BuyResultEnum resultEnum;
        try (Jedis jedis = jedisCacheUtil.getResource()) {
            //检查该用户是否已经抢购过此物品
            if (jedis.sismember(redisConfig.getProductUserListPrefix() + productId, String.valueOf(userId))) {
                return BuyResultEnum.REPEAT;
            }
            String stockValue = jedis.get(redisConfig.getProductStockPrefix() + productId);
            if (stockValue == null || Integer.parseInt(stockValue) <= 0) {
                return BuyResultEnum.FAIL;
            }
            //检测缓存中的库存是否足够
            resultEnum = this.proxy.decreaseStockAndAddRecord(userId, productId, optimisticLock) ?
                    BuyResultEnum.SUCCESS : BuyResultEnum.FAIL;
        }
        return resultEnum;
    }
}
