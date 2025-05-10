package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum GdEnum {
    RECHARGE(0,"充值"),
    WITHDRAWAL(1,"提现"),
    QUERY_ACCOUNT(2,"查询余额"),
    BETTING(3,"下注"),
    BETTING_HISTORY(4,"下注记录"),
    CHONGZHI_HISTORY(5,"充值记录"),
    TIXIAN_HISTORY(6,"提现记录");

    GdEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    private int value;
    private String name;

    public static Integer getName(String name) {
        for (GdEnum c : GdEnum.values()) {
            if (c.name.equals(name)) {
                return c.value;
            }
        }
        return null;
    }
}
