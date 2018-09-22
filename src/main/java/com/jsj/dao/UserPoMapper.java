package com.jsj.dao;

import com.jsj.pojo.entity.UserPO;
import com.jsj.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 *
 * @author jiangshenjie
 * @date 2018-9-13
 */
public interface UserPoMapper {

    boolean addUser(UserPO userPO) throws DAOException;

    UserPO getUserById(String id)throws DAOException;

    List<UserPO> searchUsers(Map<String,Object> params)throws DAOException;
}
