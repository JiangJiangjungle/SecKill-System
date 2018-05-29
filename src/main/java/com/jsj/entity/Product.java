package com.jsj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	//商品id
	private Integer productId;
	//商品库存
	private int stock;
	//商品价格
	private BigDecimal price;
	//商品名称
	private String productName;
	//创建时间
	private Date createTime;

}
