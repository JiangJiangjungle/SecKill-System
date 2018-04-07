package com.jsj.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String userId;
    private String userName;
    private String phoneNumber;
    private Date createTime;

    public User(String userId) {
        this.userId = userId;
    }
}
