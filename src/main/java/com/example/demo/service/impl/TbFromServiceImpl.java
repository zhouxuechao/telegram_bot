package com.example.demo.service.impl;

import com.example.demo.domain.TbChongzhi;
import com.example.demo.domain.TbFrom;
import com.example.demo.mapper.TbFromMapper;
import com.example.demo.service.ITbFromService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;


/**
 * 用户 业务层处理
 *
 * @author ruoyi
 */
@Service
public class TbFromServiceImpl implements ITbFromService
{
    private static final Logger log = LoggerFactory.getLogger(TbFromServiceImpl.class);

    @Resource
    private TbFromMapper tbFromMapper;

    /**
     * 通过用户id查询用户
     *
     * @param id 用户名
     * @return 用户对象信息
     */
    @Override
    public TbFrom selectByPrimaryKey(Long id) {
        return tbFromMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增
     *
     * @param from
     * @return 用户对象信息
     */
    @Override
    public int insertSelective(TbFrom from) {
        return tbFromMapper.insertSelective(from);
    }

    /**
     * 修改
     *
     * @param balance
     * @return 用户对象信息
     */
    @Override
    public int updatejianqian(BigDecimal balance, Long id) {
        return tbFromMapper.updatejianqian(balance,id);
    }

    /**
     * 修改
     *
     * @param balance
     * @return 用户对象信息
     */
    @Override
    public int updatejiaqian(BigDecimal balance, Long id) {
        return tbFromMapper.updatejiaqian(balance,id);
    }

    /**
     * 充值返回充值页面
     *
     * @param fromId  id
     * @param price
     * @param orderId
     * @return 用户对象信息
     */
    @Override
    public TbChongzhi chongzhi(Long fromId, BigDecimal price, String orderId) {
        TbChongzhi tbChongzhi = new TbChongzhi();
        tbChongzhi.setFromId(fromId);
        tbChongzhi.setPrice(price);
        tbChongzhi.setOrderId(orderId);
        return tbChongzhi;
    }

    /**
     * 提交充值订单
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public String chongzhihuidiao(String userName) {
        return null;
    }

    /**
     * 提现
     *
     * @param fromId id
     * @param price
     * @return 用户对象信息
     */
    @Override
    public String tixian(Long fromId, BigDecimal price) {
        return null;
    }


}
