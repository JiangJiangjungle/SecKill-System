package com.jsj.canal;

import com.jsj.canal.task.RedisDataUpater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
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
