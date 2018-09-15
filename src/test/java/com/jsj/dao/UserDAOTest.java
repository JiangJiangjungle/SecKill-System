package com.jsj.dao;

import com.jsj.entity.po.ProductPO;
import com.jsj.entity.po.UserPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDAOTest {
    @Resource
    private UserPoMapper userPoMapper;

    @Test
    public void testAdd(){
        UUID id = UUID.randomUUID();
        String name = "jsj";
        String phoneNumber="17620756782";
        UserPO userPO = new UserPO(id.toString(),name,phoneNumber,new Date());
        boolean finished = userPoMapper.addUser(userPO);
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
    public void testSearch(){
        Map<String,Object> map = new HashMap<>(2);
        map.put("userName","jsj");
        map.put("phone","2075");
        List<UserPO> userPOList = userPoMapper.searchUsers(map);
        userPOList.forEach(System.out::println);
    }

}
