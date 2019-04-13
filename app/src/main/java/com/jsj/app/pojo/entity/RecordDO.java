package com.jsj.app.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 交易记录的实体类
 *
 * @author jiangshenjie
 * @date 2018-9-11
 */
public class RecordDO {
    /**
     * 交易记录id(主键)
     */
    private Integer id;
    /**
     * 用户id（索引）
     */
    private String userId;
    /**
     * 商品id（索引）
     */
    private String productId;
    /**
     * 状态码
     */
    private Integer state;
    /**
     * 创建时间
     */
    private Date createTime;

    public RecordDO() {
    }

    public RecordDO(String userId, String productId, Integer state, Date createTime) {
        this.userId = userId;
        this.productId = productId;
        this.state = state;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
