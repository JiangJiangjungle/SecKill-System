package com.jsj;

import com.jsj.constant.ServiceRessult;
import com.jsj.service.PanicBuyingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

    @Resource
    private PanicBuyingService panicBuyingService;

    @Test
    public void test() throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId",1);
        paramMap.put("productId", 4);
        ServiceRessult response = panicBuyingService.handleByMySQLLock(paramMap);
        System.out.println(response.toString());
    }
}
