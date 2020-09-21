package com.born.secKill.server.service;

import com.born.secKill.model.entity.OrderStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-04-02 11:58:38
 */
public interface IOrderStatusService {

    OrderStatus getStatus(Byte statusCode);

    List<OrderStatus> listStatus();
}
