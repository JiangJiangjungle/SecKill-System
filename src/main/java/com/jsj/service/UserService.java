package com.jsj.service;

import com.jsj.pojo.entity.UserDO;
import com.jsj.exception.ServiceException;

/**
 * @author jiangshenjie
 * @date 2018-9-15
 */
public interface UserService {

    /**
     * 根据userId查询
     * @param id
     * @return
     * @throws ServiceException
     */
    UserDO searchUserById(String id) throws ServiceException;

    /**
     * 新增用户
     * @param name
     * @param phone
     * @return
     * @throws ServiceException
     */
    boolean addUser(String name,String phone) throws ServiceException;
}
