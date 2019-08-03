package com.jsj.service.dao;

import com.jsj.api.entity.UserDO;
import com.jsj.api.exception.DAOException;
import com.jsj.service.ServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ServiceApplication.class)
public class UserMapperTest {
    @Autowired
    UserMapper userMapper;

    @Test
    public void testAddUser() throws DAOException {
        UserDO userDO = new UserDO("jsj", "13888888888", new Date());
        userMapper.addUser(userDO);
    }

    @Test
    public void testUserByPrimaryId() throws DAOException {
        UserDO userDO = userMapper.getUserByPrimaryId(1L);
        System.out.println(userDO.toString());
    }

    @Test
    public void testSearchUsers() throws DAOException {
        Map<String, Object> params = new HashMap<>();
        params.put("userName","jsj");
        List<UserDO> users = userMapper.searchUsers(params);
        for (UserDO userDO : users) {
            System.out.println(userDO.toString());
        }
    }
}
