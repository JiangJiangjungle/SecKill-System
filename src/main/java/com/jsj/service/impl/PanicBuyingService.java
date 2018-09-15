package com.jsj.service.impl;

import com.jsj.util.ServiceResult;
import com.jsj.dao.ProductPoMapper;
import com.jsj.dao.RecordPoMapper;
import com.jsj.dao.UserPoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Service
public class PanicBuyingService {

    @Resource
    private ProductPoMapper productPoMapper;
    @Resource
    private RecordPoMapper recordPoMapper;
    @Resource
    private UserPoMapper userPoMapper;

    /**
     * 利用原子类作为计数器实现抢购
     *
     * @param paramMap
     * @return
     */
    @Transactional
    public ServiceResult handleByAtomicStock(Map<String, Object> paramMap) {
        return null;
    }

    /**
     * 利用redis事务支持实现抢购
     *
     * @param paramMap
     * @return
     */
    @Transactional
    public ServiceResult handleByRedisStock(Map<String, Object> paramMap) {
        return null;
    }

    /**
     * 利用MySQL行锁进行更新
     *
     * @param paramMap
     * @return
     */
    @Transactional
    public ServiceResult handleByMySQLLock(Map<String, Object> paramMap) {
        return null;
    }
}