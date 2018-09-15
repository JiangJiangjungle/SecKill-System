package com.jsj.dao;

import com.jsj.entity.po.UserPO;

import java.util.List;
import java.util.Map;

/**
 *
 * @author jiangshenjie
 * @date 2018-9-13
 */
public interface UserPoMapper {

    boolean addUser(UserPO userPO);

    UserPO getUserById(String id);

    List<UserPO> searchUsers(Map<String,Object> params);
}
