package com.jsj.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {

    private Integer userId;
    private Integer productId;
    private Integer state;
    private String stateInfo;
    private Date createTime;

}
