package com.example.demo.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TbXiazhujilu {

    private String orderId;
    private String number;
    private String qihao;
    private Integer type;
    private Long fromId;
    private String createTime;
    private String updateTime;
    private Integer status;
    private BigDecimal money;
    private BigDecimal price;

}
