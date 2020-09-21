package com.born.secKill.model.mapper;

import com.born.secKill.model.entity.SecKill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SeckillMapper {

    //查询所有被启用的秒杀商品
    List<SecKill> listActiveKill();

    //查询所有秒杀记录
    List<SecKill> listAllKill();

    //查询单条秒杀记录
    SecKill selectKillById(@Param("id") Integer id);

    //动态更新秒杀记录
    Integer updateKill(@Param("kill") SecKill kill);
    //添加秒杀记录
    Integer insertKill(@Param("kill") SecKill kill);
    //更新库存 -1
    Integer updateKillStock(@Param("killId") Integer id);

}