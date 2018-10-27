use panic_buy;

DROP TABLE IF EXISTS tb_user;
CREATE TABLE tb_user(
    id VARCHAR(64) not null comment "ID",
    user_name VARCHAR (50) not null comment "用户名",
    phone VARCHAR (20) not null comment "手机号码",
    create_time datetime not null default now() NOT NULL comment "创建时间",
    primary key (id)
) ENGINE=INNODB default charset='utf8';

DROP TABLE IF EXISTS tb_product;
CREATE TABLE `tb_product` (
  `id` varchar(64) NOT NULL COMMENT 'ID',
  `product_name` varchar(50) NOT NULL COMMENT '产品名称',
  `price` decimal(16,3) NOT NULL COMMENT '价格',
  `stock` int(64) NOT NULL COMMENT '库存',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `version_id` int(128) DEFAULT '0' COMMENT '版本id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS tb_record;
CREATE TABLE tb_record(
    id INT(128) not null AUTO_INCREMENT comment "ID",
    user_id VARCHAR(64) not null comment "用户ID",
    product_id VARCHAR(64) not null comment "产品ID",
    state tinyint(2) not null comment "秒杀状态1秒杀成功,0秒杀失败,-1重复秒杀,-2系统异常",
    create_time datetime not null default now() comment "创建时间",
    primary key (id),
    KEY user_id (user_id),
    key product_id(product_id)
)ENGINE=INNODB default charset='utf8';
