package com.jsj.canal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }
}
