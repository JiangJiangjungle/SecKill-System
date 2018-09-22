package com.jsj.dao;

import com.jsj.pojo.entity.UserPO;
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
    private UserPoMapper userPoMapper;

    @Test
    public void testAdd()throws Exception{
        for (int i=0;i<10;i++){
            UUID id = UUID.randomUUID();
            String name = "jsj"+i;
            String phoneNumber="1762075678"+i;
            UserPO userPO = new UserPO(id.toString(),name,phoneNumber,new Date());
            boolean finished = userPoMapper.addUser(userPO);
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
        List<UserPO> userPOList = userPoMapper.searchUsers(map);
        userPOList.forEach(System.out::println);
    }

}
