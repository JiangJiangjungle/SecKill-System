package com.jsj.mq.service.impl;


import com.jsj.api.entity.UserDO;
import com.jsj.api.exception.DAOException;
import com.jsj.api.exception.ServiceException;
import com.jsj.api.service.UserService;
import com.jsj.mq.dao.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author jiangshenjie
 * @date 2018-9-15
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    //    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public UserDO searchUserById(Long id) throws ServiceException {
        try {
            return userMapper.getUserByPrimaryId(id);
        } catch (DAOException d) {
            throw new ServiceException("由DAOException导致");
        }
    }

    //    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public boolean addUser(String userName, String phone) throws ServiceException {
        if (StringUtils.isEmpty(userName)) {
            throw new ServiceException("name不能为空");
        }
        if (StringUtils.isEmpty(phone)) {
            throw new ServiceException("phone不能为空");
        }
        UserDO userDO = new UserDO(userName, phone, new Date());
        try {
            return userMapper.addUser(userDO);
        } catch (DAOException d) {
            throw new ServiceException();
        }
    }
}
