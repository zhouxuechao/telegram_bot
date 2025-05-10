package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum MessageEnum {
    PRIVATE(0,"private"),
    GROUP(1,"supergroup");
//    GROUP(1,"group");

    MessageEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    private int value;
    private String name;
}
