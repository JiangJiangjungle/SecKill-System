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
CREATE TABLE tb_product(
    id VARCHAR(64) not null comment "ID",
    product_name VARCHAR (50) not null comment "产品名称",
    price DECIMAL(16,3) not null comment "价格",
    stock INT(64) not null comment "库存",
    create_time datetime not null default now() comment "创建时间",
    primary key (id)
)ENGINE=INNODB default charset='utf8';

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
