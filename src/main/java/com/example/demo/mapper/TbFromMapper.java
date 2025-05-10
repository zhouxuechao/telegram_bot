package com.example.demo.mapper;


import com.example.demo.domain.TbFrom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 用户表 数据层
 *
 * @author ruoyi
 */
public interface TbFromMapper
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
    public int updatejianqian(@Param("balance") BigDecimal balance,@Param("id") Long id);
    /**
     * 修改
     *
     * @return 用户对象信息
     */
    public int updatejiaqian(@Param("balance") BigDecimal balance,@Param("id") Long id);


}
