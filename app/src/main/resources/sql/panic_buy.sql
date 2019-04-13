use sec_kill;

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
    `id` int(32) not null AUTO_INCREMENT comment 'ID',
    `user_id` int(32) not null comment '用户ID',
    `product_id` int(32) not null comment '产品ID',
    `state` tinyint(3) not null comment '秒杀状态: 1秒杀成功,0秒杀失败,-1重复秒杀,-2系统异常',
    `create_time` datetime not null default now() comment '创建时间',
    primary key (`id`),
    key key_product_id(`product_id`),
    key key_user_id_product_id (`user_id`,`product_id`)
)ENGINE=InnoDB default charset='utf8';
