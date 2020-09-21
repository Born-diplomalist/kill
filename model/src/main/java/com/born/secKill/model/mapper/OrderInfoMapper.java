package com.born.secKill.model.mapper;

import com.born.secKill.model.dto.KillSuccessUserInfo;
import com.born.secKill.model.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface OrderInfoMapper {

    int deleteByPrimaryKey(String code);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(String code);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);

    int countByKillUserId(@Param("killId") Integer killId, @Param("userId") Integer userId);

    KillSuccessUserInfo selectDetailResultByCode(@Param("code") String code);

    int expireOrder(@Param("code") String code);

    /**
     * 查询所有未付款订单
     */
    List<OrderInfo> listExpireOrder();

    /**
     * 获取所有有效订单（未被失效处理的）
      * @return
     */
    List<OrderInfo> listEffectiveOrder();


    int countOrder();

    List<KillSuccessUserInfo> listDetailResult(@Param("pageStartIndex")Integer pageStartIndex, @Param("pageSize")Integer pageSize);
}