package com.born.secKill.server.service;/**
 * Created by Administrator on 2020/3/29.
 */

import com.born.secKill.model.entity.OrderInfo;
import com.born.secKill.model.entity.SecKill;
import com.born.secKill.model.mapper.SeckillMapper;
import com.born.secKill.model.mapper.OrderInfoMapper;
import com.born.secKill.server.common.ConstantClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务服务
 * @Author:gyk
 * @Date: 2020/3/29 21:26
 **/
@Service
public class SchedulerService {

    private static final Logger log= LoggerFactory.getLogger(SchedulerService.class);

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private Environment env;

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 定时获取status=0的订单并判断是否超过TTL，然后进行订单失效
     *
     * 可避免RabbitMQ宕机造成的大量失效订单集中失效问题
     */
    @Scheduled(cron = "0 0/60 * * * ?")//cron表达式，每60分钟执行一次
    public void schedulerExpireOrders(){

        try {
            List<OrderInfo> list= orderInfoMapper.listExpireOrder();
            if (list!=null && !list.isEmpty()){
                for (OrderInfo orderInfo:list) {
                    if (orderInfo!=null && orderInfo.getDiffTime() > env.getProperty("scheduler.expire.orders.time",Integer.class)){
                        orderInfoMapper.expireOrder(orderInfo.getCode());
                         SecKill secKill = new SecKill();
                        secKill.setTotal(secKill.getTotal()+1);
                        if (seckillMapper.updateKill(secKill)>0){
                            redisTemplate.opsForHash().increment(orderInfo.getKillId().toString(),orderInfo.getUserId(),1);
                        }
                        throw new Exception("定时任务回补库存失败");
                    }
                }
            }
        }catch (Exception e){
            log.error("定时获取status=0的订单并判断是否超过TTL，然后进行失效-发生异常：",e.fillInStackTrace());
        }
    }


    /**
     * 定期更新redis缓存数据
     */
    @Scheduled(cron = "0/3 * * * * ?")//cron表达式，每3秒执行一次
    public void updateRedisInfo(){
        HashOperations opsForHash = redisTemplate.opsForHash();

        //字段1
        //缓存已经秒杀的有效订单记录
        List<OrderInfo> orderInfoList = orderInfoMapper.listEffectiveOrder();
         if (orderInfoList!=null && !orderInfoList.isEmpty()) {
            for (OrderInfo orderInfo:orderInfoList) {
                String key =orderInfo.getKillId().toString();
                opsForHash.put(key, orderInfo.getUserId(), ConstantClass.USER_HAS_SECKILLED.toString());
            }
        }

        //字段2,3,4
        List<SecKill> secKillList = seckillMapper.listActiveKill();
        if (secKillList!=null && !secKillList.isEmpty()) {
            for (SecKill secKill:secKillList) {
                String key =secKill.getId().toString();
                opsForHash.put(key, "total", secKill.getTotal().toString());
                opsForHash.put(key, "start_time", String.valueOf(secKill.getStartTime().getTime()));
                opsForHash.put(key, "end_time", String.valueOf(secKill.getEndTime().getTime()));
            }
        }
    }



    /**
     *以Redis作为最新数据存储--已弃用
     * 更新库存到数据库
     * 1. 查询秒杀表
     * 2. 以每个秒杀表的秒杀id为 key，分别去获取redis中对应的最新库存，并执行更新操作
     * 此处注意一个细节：回补库存时记得将redis中的对应库存也+1，保证redis库存是最新的，通过redis往mysql更新
     * 回补库存本项目有两处实现，一个是死信队列消费方，一个是定时任务
     */
    //@Scheduled(cron = "0/30 * * * * ?")//cron表达式，每30s执行一次
    //public void updateStockFromRedisToMysql(){
    //    //
    //    List<SecKill> secKillList = seckillMapper.listActiveKill();
    //    if (secKillList!=null && !secKillList.isEmpty()) {
    //        for (SecKill secKill:secKillList) {
    //            String key =secKill.getId().toString();
    //            String totalStr = (String) redisTemplate.opsForHash().get(key, "total");
    //            secKill.setTotal(Integer.valueOf(totalStr));
    //            seckillMapper.updateKill(secKill);
    //        }
    //    }
    //}

}




































