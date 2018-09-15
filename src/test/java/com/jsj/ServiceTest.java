package com.jsj;

import com.jsj.util.ServiceResult;
import com.jsj.service.PanicBuyService;
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
    private PanicBuyService panicBuyService;


}
