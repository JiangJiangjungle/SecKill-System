package com.jsj.service.impl;

import com.jsj.dao.UserPoMapper;
import com.jsj.entity.UserPO;
import com.jsj.exception.BaseException;
import com.jsj.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;


@Service
@Alias("userService")
/**
 * @author jiangshenjie
 * @date 2018-9-15
 */
public class UserServiceImpl implements UserService {
    @Resource
    private UserPoMapper userPoMapper;

    @Transactional(rollbackFor = BaseException.class)
    @Override
    public UserPO searchUserById(String id) throws BaseException{
        if(StringUtils.isEmpty(id)){
            throw new BaseException("id不能为空");
        }
        return userPoMapper.getUserById(id);
    }

    @Transactional(rollbackFor = BaseException.class)
    @Override
    public boolean addUser(String userName, String phone) throws BaseException {
        if (StringUtils.isEmpty(userName)) {
            throw new BaseException("name不能为空");
        }
        if (StringUtils.isEmpty(phone)) {
            throw new BaseException("phone不能为空");
        }
        String userId = UUID.randomUUID().toString();
        UserPO userPO = new UserPO(userId,userName,phone,new Date());
        return userPoMapper.addUser(userPO);
    }
}
