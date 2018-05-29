package com.jsj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 交易记录的实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {
    //用户id
    private Integer userId;
    //商品id
    private Integer productId;
    //状态码
    private Integer state;
    //创建时间
    private Date createTime;

}
