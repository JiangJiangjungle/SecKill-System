package com.jsj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    //用户id
    private String userId;
    //用户名
    private String userName;
    //手机号码
    private String phoneNumber;
    //创建时间
    private Date createTime;

    public User(String userId) {
        this.userId = userId;
    }
}
