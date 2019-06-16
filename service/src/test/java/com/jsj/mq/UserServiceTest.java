package com.jsj.mq;

import com.jsj.api.entity.UserDO;
import com.jsj.api.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ServiceApplication.class)
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    public void testaddUser() throws Exception {
        boolean succeed = userService.addUser("lalala","17666666666");
        System.out.println(succeed?"添加用户成功":"添加用户失败");
    }

    @Test
    public void testSearchUserById() throws Exception {
        UserDO userDO =userService.searchUserById(1L);
        System.out.println(userDO.toString());
    }
}
