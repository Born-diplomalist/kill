package com.born.secKill.model.mapper;

import com.born.secKill.model.entity.OrderStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-04-02 11:20:14
 */
@Mapper
public interface OrderStatusMapper {

    OrderStatus getStatus(@Param("statusCode") Byte statusCode);

    List<OrderStatus> listStatus();

}
