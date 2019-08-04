package com.jsj.canal.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiangshenjie
 */
@Data
@Configuration
public class CanalSubscribeConfig {

    @Value("${data.canal-subscribe.tb_product_id}")
    private String productId;

    @Value("${data.canal-subscribe.tb_product_stock}")
    private String productStock;

    @Value("${data.canal-subscribe.tb_record_user_id}")
    private String recordUserId;

    @Value("${data.canal-subscribe.tb_record_product_id}")
    private String recordProductId;
}
