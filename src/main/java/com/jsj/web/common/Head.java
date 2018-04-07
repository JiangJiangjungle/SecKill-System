package com.jsj.web.common;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author twc
 */
@Data
@NoArgsConstructor
public class Head {

    /**
     * 状态码
     */
    private int statusCode;

    /**
     * 状态信息
     */
    private String statusMessage;

}
