package com.jsj.dao;

import com.jsj.pojo.entity.UserDO;
import com.jsj.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 *
 * @author jiangshenjie
 * @date 2018-9-13
 */
public interface UserMapper {

    boolean addUser(UserDO userDO) throws DAOException;

    UserDO getUserById(String id)throws DAOException;

    List<UserDO> searchUsers(Map<String,Object> params)throws DAOException;
}
