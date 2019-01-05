# panic-buy

一个基于Spring Boot的高并发秒杀抢购解决方案，用于个人学习。欢迎提供意见和建议
 
### 项目结构
 - 服务注册和发现中心：**Eureka**
 - API网关：**Zuul**
 - 缓存：**Redis**
 - 消息队列：**Kafka**
 - 数据库：**MySQL**
 
 -------------------
 
### SQL

DROP TABLE IF EXISTS tb_user;
CREATE TABLE `tb_user`(
    `id` int(32) not null AUTO_INCREMENT comment 'ID',
    `user_name` varchar (50) not null comment '用户名',
    `phone` varchar (20) not null comment '手机号码',
    `create_time` datetime not null default now() NOT NULL comment "创建时间",
    primary key (`id`)
) ENGINE=InnoDB default charset='utf8';

DROP TABLE IF EXISTS tb_product;
CREATE TABLE `tb_product` (
  `id` int(32) not null AUTO_INCREMENT COMMENT 'ID',
  `product_name` varchar(50) not null COMMENT '产品名称',
  `price` decimal(16,3) not null COMMENT '价格',
  `stock` int(64) not null COMMENT '库存',
  `create_time` datetime not null default CURRENT_TIMESTAMP COMMENT '创建时间',
  `version_id` int(128) default '0' COMMENT '版本id',
  primary key (`id`)
) ENGINE=InnoDB DEFAULT CHARSET='utf8';

DROP TABLE IF EXISTS tb_record;
CREATE TABLE `tb_record`(
    `id` int(64) not null AUTO_INCREMENT comment 'ID',
    `user_id` int(32) not null comment '用户ID',
    `product_id` int(32) not null comment '产品ID',
    `state` tinyint(3) not null comment '秒杀状态: 1秒杀成功,0秒杀失败,-1重复秒杀,-2系统异常',
    `create_time` datetime not null default now() comment '创建时间',
    primary key (`id`),
    key key_user_id (`user_id`),
    key key_product_id(`product_id`),
    key key_user_id_product_id (`user_id`,`product_id`)
)ENGINE=InnoDB default charset='utf8';

-------------------
 
### 主要特性

#### 缓存查询

预加载商品库存到Redis缓存，提高查询速率

#### 数据一致性

提供3种加锁方案,并且在一个事务中：MySQL商品库存更新后，立即更新缓存中的库存

 - **MySQL乐观锁**
 - **Redis锁**
 - **ZooKeeper锁**

#### 异步通信

利用Kafka实现交易记录的生产和异步消费，降低请求响应时间

#### 限流

利用zuul-ratelimit提供的令牌桶算法，在API网关层实现限流

-------------------
