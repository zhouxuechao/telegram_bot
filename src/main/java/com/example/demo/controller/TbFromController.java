package com.example.demo.controller;

import com.example.demo.service.ITbFromService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class TbFromController {

    @Autowired
    ITbFromService iSysUserService;
    /**
     * 验证码生成
     */
    @GetMapping(value = "/user")
    @ResponseBody
    public Long getuser(String userName)
    {
       return null;
    }
}
