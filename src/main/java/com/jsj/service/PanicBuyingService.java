package com.jsj.service;

import com.jsj.exception.BaseException;
import com.jsj.util.ServiceResult;
import com.jsj.dao.ProductPoMapper;
import com.jsj.dao.RecordPoMapper;
import com.jsj.dao.UserPoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

public interface PanicBuyingService {

    /**
     * 利用MySQL行锁进行更新
     *
     * @param paramMap
     * @return
     */
    ServiceResult handleByMySQLLock(Map<String, Object> paramMap) throws BaseException;
}