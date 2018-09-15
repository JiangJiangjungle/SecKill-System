package com.jsj.service;

import com.jsj.entity.UserPO;
import com.jsj.exception.BaseException;

/**
 * @author jiangshenjie
 * @date 2018-9-15
 */
public interface UserService {

    /**
     * 根据userId查询
     * @param id
     * @return
     * @throws BaseException
     */
    UserPO searchUserById(String id) throws BaseException;

    /**
     * 新增用户
     * @param name
     * @param phone
     * @return
     * @throws BaseException
     */
    boolean addUser(String name,String phone) throws BaseException;
}
