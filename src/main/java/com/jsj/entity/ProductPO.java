package com.jsj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;
import org.springframework.stereotype.Component;

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
public class ProductPO {
	/**
	 * 商品id（主键）
	 */
	private String id;
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

}
