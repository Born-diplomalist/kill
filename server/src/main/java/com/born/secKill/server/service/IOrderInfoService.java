package com.born.secKill.server.service;

import com.born.secKill.model.dto.KillSuccessUserInfo;

import java.util.List;

/**
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-04-01 21:15:31
 */
public interface IOrderInfoService {

    //List<OrderInfo> listOrderInfo();

    int getOrderNum();

    KillSuccessUserInfo getOrderDetailResult(String code);

    List<KillSuccessUserInfo> listOrderDetailResult(Integer pageStartIndex, Integer pageSize);

    boolean modifyOrderStatus(String orderNo);
}
