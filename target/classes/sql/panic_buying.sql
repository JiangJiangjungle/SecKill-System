use panic_buying;

DROP TABLE IF EXISTS tb_user;
CREATE TABLE tb_user(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY comment "ID",
    user_name VARCHAR (20) UNIQUE NOT NULL comment "用户名",
    phone VARCHAR (20) NOT NULL comment "手机号码",
    create_time datetime NOT NULL default now() NOT NULL comment "创建时间"
);

DROP TABLE IF EXISTS tb_product;
CREATE TABLE tb_product(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY comment "ID",
    product_name VARCHAR (20) UNIQUE NOT NULL comment "产品名称",
    price DECIMAL(16,3) NOT NULL comment "价格",
    stock INT NOT NULL comment "库存",
    create_time datetime NOT NULL default now() comment "创建时间"
);

DROP TABLE IF EXISTS tb_record;
CREATE TABLE tb_record(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY comment "ID",
    user_id INT NOT NULL comment "用户ID",
    product_id INT NOT NULL comment "产品ID",
    state int(2) NOT NULL comment "秒杀状态1秒杀成功,0秒杀失败,-1重复秒杀,-2系统异常",
    create_time datetime NOT NULL default now() comment "创建时间"
);
