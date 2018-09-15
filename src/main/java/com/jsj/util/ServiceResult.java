package com.jsj.util;

/**
 * 用于返回秒杀结果
 */
public enum ServiceResult {
    //秒杀成功
    SUCCESS(1, "秒杀成功"),
    //秒杀失败
    FAIL(0, "秒杀失败"),
    //重复秒杀
    REPEAT(-1, "重复秒杀"),
    //参数错误
    PARAMS_ERROR(-2,"参数错误"),
    //系统错误
    SYSTEM_EXCEPTION(-3, "系统错误");

    private Integer value;
    private String label;

    ServiceResult(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
