package com.jsj.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息实体类
 *
 * @author jiangshenjie
 * @date 2018-9-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDO implements Serializable {
    /**
     * 用户id(主键)
     */
    private Long id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 创建时间
     */
    private Date createTime;

    public UserDO(String userName, String phone) {
        this.userName = userName;
        this.phone = phone;
    }

    public UserDO(String userName, String phone, Date createTime) {
        this.userName = userName;
        this.phone = phone;
        this.createTime = createTime;
    }
}
