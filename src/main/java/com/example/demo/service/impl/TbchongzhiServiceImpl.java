package com.example.demo.service.impl;

import com.example.demo.domain.TbChongzhi;
import com.example.demo.domain.TbFrom;
import com.example.demo.mapper.TbFromMapper;
import com.example.demo.mapper.TbchongzhiMapper;
import com.example.demo.service.ITbFromService;
import com.example.demo.service.ITbchongzhiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


/**
 * 用户 业务层处理
 *
 * @author ruoyi
 */
@Service
public class TbchongzhiServiceImpl implements ITbchongzhiService
{
    private static final Logger log = LoggerFactory.getLogger(TbchongzhiServiceImpl.class);

    @Resource
    private TbchongzhiMapper tbchongzhiMapper;


    /**
     * 通过订单号查询订单
     *
     * @param fromId 订单号
     * @param type 订单号
     * @return 用户对象信息
     */
    @Override
    public List<TbChongzhi> selectList(Long fromId, Integer type,Integer limit) {
        return tbchongzhiMapper.selectList(fromId,type,limit);
    }

    /**
     * 新增
     *
     * @param from
     * @return 用户对象信息
     */
    @Override
    public int insertSelective(TbChongzhi from) {
        return tbchongzhiMapper.insertSelective(from);
    }

    /**
     * 修改
     *
     * @param orderId
     * @return 用户对象信息
     */
    @Override
    public int updateByPrimaryKeySelective(String orderId) {
        return tbchongzhiMapper.updateByPrimaryKeySelective(orderId);
    }
}
