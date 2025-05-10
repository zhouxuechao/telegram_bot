package com.example.demo.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
@Data
public class TbFrom implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Date createTime;

    private Long id;

    private String firstName;

    private Boolean isBot;

    private String lastName;

    private String userName;

    private String languageCode;

    private BigDecimal balance;
}
