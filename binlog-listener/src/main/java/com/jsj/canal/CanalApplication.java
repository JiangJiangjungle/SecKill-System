package com.jsj.canal;

import com.jsj.canal.task.RedisDataUpdater;
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
public class CanalApplication {
    @Autowired
    private RedisDataUpdater redisDataUpdater;

    @PostConstruct
    public void start() {
        redisDataUpdater.start();
    }

    public static void main(String[] args) {
        SpringApplication.run(CanalApplication.class, args);
    }
}
