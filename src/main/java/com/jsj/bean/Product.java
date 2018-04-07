package com.jsj.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Integer productId;
    private int stock;
    private BigDecimal price;
    private String productName;
    private String detail;
    private Date createTime;

}
