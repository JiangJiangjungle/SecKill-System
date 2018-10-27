package com.jsj.app.dao;

import com.jsj.app.exception.DAOException;
import com.jsj.app.pojo.entity.UserDO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 * @author jiangshenjie
 * @date 2018-9-13
 */
@Repository
public interface UserMapper {

    boolean addUser(UserDO userDO) throws DAOException;

    UserDO getUserById(String id)throws DAOException;

    List<UserDO> searchUsers(Map<String, Object> params)throws DAOException;
}
