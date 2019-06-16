package com.jsj.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 交易记录的实体类
 *
 * @author jiangshenjie
 * @date 2018-9-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordDO implements Serializable {
    /**
     * 交易记录id(主键)
     */
    private Long id;
    /**
     * 用户id（索引）
     */
    private Long userId;
    /**
     * 商品id（索引）
     */
    private Long productId;
    /**
     * 状态码
     */
    private Integer state;
    /**
     * 创建时间
     */
    private Date createTime;

    public RecordDO(Long userId, Long productId, Integer state, Date createTime) {
        this.userId = userId;
        this.productId = productId;
        this.state = state;
        this.createTime = createTime;
    }
}
