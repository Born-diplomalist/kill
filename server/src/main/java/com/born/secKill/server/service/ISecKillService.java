package com.born.secKill.server.service;

import com.born.secKill.model.entity.SecKill;
import com.born.secKill.server.utils.JsonBean;

import java.util.List;

public interface ISecKillService {

    List<SecKill> listAllKill() ;

    SecKill getKillDetail(Integer id) throws Exception;

    /**
     * 秒杀
     */
    JsonBean<Object> killItem(Integer killId, Integer userId) throws Exception;

    Integer addKill(SecKill kill);

    Integer modifyKill(SecKill kill);

    /**
     * 更改订单状态
     */
    Integer changeActiveStatus(Integer id);

    List<SecKill> listActiveKill();
}
