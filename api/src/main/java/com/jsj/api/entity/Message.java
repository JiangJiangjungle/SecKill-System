package com.jsj.api.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jsj
 */
@Data
@NoArgsConstructor
public class Message<T> {

    /**
     * 状态码
     */
    private Integer statusCode;

    /**
     * 状态信息
     */
    private String statusMessage;

    /**
     * 其他
     */
    private T body;

}
