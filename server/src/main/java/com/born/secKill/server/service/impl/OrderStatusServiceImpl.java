package com.born.secKill.server.service.impl;

import com.born.secKill.model.entity.OrderStatus;
import com.born.secKill.model.mapper.OrderStatusMapper;
import com.born.secKill.server.service.IOrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-04-02 11:59:15
 */
@Service
public class OrderStatusServiceImpl implements IOrderStatusService {

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Override
    public OrderStatus getStatus(Byte statusCode) {
        return orderStatusMapper.getStatus(statusCode);
    }

    @Override
    public List<OrderStatus> listStatus() {
        return orderStatusMapper.listStatus();
    }
}
