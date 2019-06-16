package com.jsj.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品实体类
 *
 * @author jiangshenjie
 * @date 2018-9-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDO implements Serializable {
	/**
	 * 商品id（主键）
	 */
	private Long id;
	/**
	 * 商品库存
	 */
	private Integer stock;
	/**
	 * 商品价格
	 */
	private BigDecimal price;
	/**
	 * 商品名称
	 */
	private String productName;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 版本号
	 */
	private Integer versionId;

	public ProductDO(Integer stock, BigDecimal price, String productName) {
		this.stock = stock;
		this.price = price;
		this.productName = productName;
	}

	public ProductDO(Integer stock, BigDecimal price, String productName, Date createTime) {
		this.stock = stock;
		this.price = price;
		this.productName = productName;
		this.createTime = createTime;
	}
}
