package com.example.demo.service;

import com.example.demo.domain.TbChongzhi;

import java.util.List;

/**
 * 用户 业务层
 *
 * @author ruoyi
 */
public interface ITbchongzhiService
{

    /**
     * 通过订单号查询订单
     * @param fromId 订单号
     * @param type 订单号
     * @return 用户对象信息
     */
    public List<TbChongzhi> selectList(Long fromId, Integer type,Integer limit);

    /**
     * 新增
     *
     * @return 用户对象信息
     */
    public int insertSelective(TbChongzhi from);

    /**
     * 修改
     *
     * @return 用户对象信息
     */
    public int updateByPrimaryKeySelective(String orderId);
}
