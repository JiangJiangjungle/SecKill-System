package com.jsj.dao;

import com.jsj.pojo.entity.UserDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDAOTest {
    @Resource
    private UserMapper userMapper;

    @Test
    public void testAdd()throws Exception{
        for (int i=0;i<10;i++){
            UUID id = UUID.randomUUID();
            String name = "jsj"+i;
            String phoneNumber="1762075678"+i;
            UserDO userDO = new UserDO(id.toString(),name,phoneNumber,new Date());
            boolean finished = userMapper.addUser(userDO);
        }

    }

    @Test
    public void testDelete(){

    }

    @Test
    public void testUpdate(){

    }

    @Test
    public void testSearchByPrimaryId(){

    }

    @Test
    public void testSearch()throws Exception{
        Map<String,Object> map = new HashMap<>(2);
        map.put("userName","jsj");
        map.put("phone","2075");
        List<UserDO> userDOList = userMapper.searchUsers(map);
        userDOList.forEach(System.out::println);
    }

}
