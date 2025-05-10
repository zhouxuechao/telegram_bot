package com.example.demo.service;

import com.example.demo.domain.TbChongzhi;
import com.example.demo.domain.TbFrom;

import java.math.BigDecimal;

/**
 * 用户 业务层
 *
 * @author ruoyi
 */
public interface ITbFromService
{

    /**
     * 通过用户id查询用户
     *
     * @param id 用户名
     * @return 用户对象信息
     */
    public TbFrom selectByPrimaryKey(Long id);

    /**
     * 新增
     *
     * @return 用户对象信息
     */
    public int insertSelective(TbFrom from);

    /**
     * 修改
     *
     * @return 用户对象信息
     */
    public int updatejiaqian(BigDecimal balance, Long id);
    /**
     * 修改
     *
     * @return 用户对象信息
     */
    public int updatejianqian(BigDecimal balance, Long id);


    /**
     * 充值返回充值页面
     *
     * @param fromId id
     * @return 用户对象信息
     */
    public TbChongzhi chongzhi(Long fromId, BigDecimal price, String orderId);

    /**
     * 提交充值订单
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    public String chongzhihuidiao(String userName);

    /**
     * 提现
     *
     * @param fromId id
     * @return 用户对象信息
     */
    public String tixian(Long fromId, BigDecimal price);

}
