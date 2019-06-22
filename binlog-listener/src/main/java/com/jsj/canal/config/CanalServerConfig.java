package com.jsj.canal.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class CanalServerConfig {

    @Value("${canal-server.host}")
    private String host;

    @Value("${canal-server.port}")
    private int port;

    @Value("${canal-server.destination}")
    private String destination;

    @Value("${canal-server.username}")
    private String username;

    @Value("${canal-server.password}")
    private String password;

    @Value("${canal-server.subscribe}")
    private String subscribe;

}
