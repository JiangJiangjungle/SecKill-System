package com.jsj.config;

import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@Configuration
@PropertySource("classpath:application.properties")
@Data
public class RabbitmqConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.username}")
    private String userName;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.port}")
    private Integer port;
    @Value("${spring.rabbitmq.publisher-confirms}")
    private boolean publisherConfirms;
    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;


    @Value("${spring.rabbitmq.template.exchange}")
    private String exchangeName;
    @Value("${spring.rabbitmq.template.queueName}")
    private String queueName;
    @Value("${spring.rabbitmq.template.routingKey}")
    private String routingKey;

    //创建工厂连接
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(this.host);
        connectionFactory.setUsername(this.userName);
        connectionFactory.setPassword(this.password);
        connectionFactory.setVirtualHost(this.virtualHost);
        connectionFactory.setPublisherConfirms(this.publisherConfirms); //必须要设置
        return connectionFactory;
    }

    //rabbitmq的模板配置
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) //必须是prototype类型
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(this.connectionFactory());
        //template.setConfirmCallback(); 设置消息确认
        //template.setReturnCallback();
        return template;
    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * <p>
     * <p>
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     */
    @Bean(name = "directExchange")
    public DirectExchange defaultExchange() {
        return new DirectExchange(exchangeName);
    }

    //队列
    @Bean
    public Queue queue() {
        return QueueBuilder.durable(this.queueName).build();//队列持久
    }

    //绑定
    @Bean
    public Binding binding(@Qualifier("queue") Queue queue, @Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(this.routingKey);
    }

}
