package com.born.secKill.server.service.impl;

import com.born.secKill.model.dto.KillSuccessUserInfo;
import com.born.secKill.model.entity.OrderInfo;
import com.born.secKill.model.entity.OrderStatus;
import com.born.secKill.model.mapper.OrderInfoMapper;
import com.born.secKill.model.mapper.OrderStatusMapper;
import com.born.secKill.server.common.ConstantClass;
import com.born.secKill.server.service.IOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-04-01 22:40:39
 */
@Service
public class OrderInfoServiceImpl implements IOrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Override
    public int getOrderNum() {
        return orderInfoMapper.countOrder();
    }

    @Override
    public KillSuccessUserInfo getOrderDetailResult(String code) {
        KillSuccessUserInfo killSuccessUserInfo = orderInfoMapper.selectDetailResultByCode(code);
        //OrderStatus orderStatus = orderStatusMapper.getStatus(killSuccessUserInfo.getStatusCode());
        //killSuccessUserInfo.setStatusMsg(orderStatus.getStatusMsg());
        killSuccessUserInfo.setStatusMsg(orderStatusMapper.getStatus(killSuccessUserInfo.getStatusCode()).getStatusMsg());
        return killSuccessUserInfo;
    }

    @Override
    public List<KillSuccessUserInfo> listOrderDetailResult(Integer pageStartIndex, Integer pageSize) {
        List<KillSuccessUserInfo> killSuccessUserInfoList = orderInfoMapper.listDetailResult(pageStartIndex, pageSize);
        //查出订单状态码对应的状态的文本加进去
        for (KillSuccessUserInfo killSuccessUserInfo: killSuccessUserInfoList) {
            killSuccessUserInfo.setStatusMsg(orderStatusMapper.getStatus(killSuccessUserInfo.getStatusCode()).getStatusMsg());
        }
        return killSuccessUserInfoList;
    }

    @Override
    public boolean modifyOrderStatus(String orderNo) {
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderNo);
        orderInfo.setCode(orderNo);
        return orderInfoMapper.updateByPrimaryKey(orderInfo) > 0;
    }
}
