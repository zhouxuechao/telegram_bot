package com.example.demo.service;

import com.example.demo.domain.TbXiazhujilu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 业务层
 *
 * @author ruoyi
 */
public interface ITxiazhulishiService
{

    /**
     * 通过订单号查询订单
     *
     * @return 用户对象信息
     */
    public List<TbXiazhujilu> selectAllList();

    /**
     * 通过订单号查询订单
     * @param fromId 订单号
     * @return 用户对象信息
     */
    public List<TbXiazhujilu> selectList(Long fromId,Integer limit);

    /**
     * 新增
     *
     * @return 用户对象信息
     */
    public int insertSelective(TbXiazhujilu from);

    /**
     * 修改
     *
     * @return 用户对象信息
     */
    public int updateByPrimaryKeySelective(@Param("status") Integer status, @Param("orderId") String orderId);
}
