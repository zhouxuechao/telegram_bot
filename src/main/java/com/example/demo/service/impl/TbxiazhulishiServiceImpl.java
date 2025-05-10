package com.example.demo.service.impl;

import com.example.demo.domain.TbXiazhujilu;
import com.example.demo.mapper.TbxiazhulishiMapper;
import com.example.demo.service.ITxiazhulishiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 用户 业务层处理
 *
 * @author ruoyi
 */
@Service
public class TbxiazhulishiServiceImpl implements ITxiazhulishiService
{
    private static final Logger log = LoggerFactory.getLogger(TbxiazhulishiServiceImpl.class);

    @Resource
    private TbxiazhulishiMapper tbxiazhulishiMapper;

    /**
     * 通过订单号查询订单
     *
     * @return 用户对象信息
     */
    @Override
    public List<TbXiazhujilu> selectAllList() {
        return tbxiazhulishiMapper.selectAllList();
    }


    /**
     * 通过订单号查询订单
     *
     * @param fromId 订单号
     * @return 用户对象信息
     */
    @Override
    public List<TbXiazhujilu> selectList(Long fromId,Integer limit) {
        return tbxiazhulishiMapper.selectList(fromId,limit);
    }

    /**
     * 新增
     *
     * @param from
     * @return 用户对象信息
     */
    @Override
    public int insertSelective(TbXiazhujilu from) {
        return tbxiazhulishiMapper.insertSelective(from);
    }

    /**
     * 修改
     *
     * @param status
     * @param orderId
     * @return 用户对象信息
     */
    @Override
    public int updateByPrimaryKeySelective(Integer status, String orderId) {
        return tbxiazhulishiMapper.updateByPrimaryKeySelective(status,orderId);
    }
}
