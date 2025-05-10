package com.example.demo.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TbChongzhi {

    private Long fromId;
    private BigDecimal price;
    private String orderId;
    private Integer status;
    private Integer type;
    private String dizhi;
    private String createTime;
    private String updateTime;
}
