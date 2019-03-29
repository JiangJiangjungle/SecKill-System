package com.jsj.app.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ZooKeeperConfig {

    @Value("${data.zookeeper.host}")
    private String host;
    @Value("${data.zookeeper.port}")
    private String port;
    @Value("${data.zookeeper.timeout}")
    private int timeout;
    @Value("${data.zookeeper.lock-key}")
    private String lockKey;
    @Value("${data.zookeeper.lock-namespace}")
    private String lockNameSpace;
}
