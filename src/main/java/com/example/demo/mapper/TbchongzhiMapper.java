package com.example.demo.mapper;


import com.example.demo.domain.TbChongzhi;
import com.example.demo.domain.TbFrom;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户表 数据层
 *
 * @author ruoyi
 */
public interface TbchongzhiMapper
{
    /**
     * 通过订单号查询订单
     * @param fromId 订单号
     * @param type 订单号
     * @return 用户对象信息
     */
    public List<TbChongzhi> selectList(@Param("fromId") Long fromId,@Param("type") Integer type,@Param("limit") Integer limit);

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
