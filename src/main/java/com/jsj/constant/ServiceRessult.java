package com.jsj.constant;

public enum ServiceRessult {

    /**
     * 服务级错误
     */
    SUCCESS(1, "秒杀成功"),
    FAIL(0, "秒杀失败"),
    REPEAT(-1, "重复秒杀"),
    SYSTEM_EXCEPTION(-2, "系统错误");

    private int value;
    private String label;

    ServiceRessult(int value, String label) {
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
