package com.jsj.canal;

import com.jsj.canal.task.RedisDataUpater;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@MapperScan("com.jsj.canal.dao")
/**
 * @author jiangshenjie
 * @date 2018-9-21
 */
public class Application {

    @Autowired
    private RedisDataUpater redisDataUpater;

    @PostConstruct
    public void start() {
        redisDataUpater.start();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
