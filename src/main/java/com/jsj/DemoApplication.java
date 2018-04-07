package com.jsj;


import com.jsj.service.PanicBuyingService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@SpringBootApplication
@MapperScan("com.jsj.mapper")//将项目中对应的mapper类的路径加进来就可以了
public class DemoApplication {

    @Resource
    private PanicBuyingService panicBuyingService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @RequestMapping("/")
    String index() {
        return "Hello Spring Boot";
    }
}
