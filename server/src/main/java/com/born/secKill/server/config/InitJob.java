package com.born.secKill.server.config;

import com.born.secKill.model.entity.OrderInfo;
import com.born.secKill.model.entity.SecKill;
import com.born.secKill.model.mapper.OrderInfoMapper;
import com.born.secKill.model.mapper.SeckillMapper;
import com.born.secKill.server.common.ConstantClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: 容器初始化时的操作
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-04-04 10:18:42
 */
@Component
public class InitJob  implements ApplicationRunner {


    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 初始化数据库信息到Redis中
     *
     *
     * 该方法应该在容器启动时执行一次
     * 而且不能被执行多次，否则mysql信息会覆盖redis的信息
     * 因为我们现在是拿redis做最新数据库使用，最新数据在redis，mysql信息只做持久化操作
        //字段1：   秒杀id：用户id：秒杀数量  ---获取秒杀数量，当数量为1时，说明该用户已抢购了，当数量为0时，可以抢购；这个属性应该在秒杀时再添加
        //字段2：   秒杀id：库存   ---定时更新到mysql即可
        //字段3：   秒杀id：开始时间  为了保证cankill的精确性，将这个值放进redis，这样可以避免cankill频繁走数据库逻辑，而且mysql和redis不同步，这在判断时间的问题上是致命的
        //字段4：   秒杀id：结束时间  为了保证cankill的精确性，将这个值放进redis，这样可以避免cankill频繁走数据库逻辑，而且mysql和redis不同步，这在判断时间的问题上是致命的
        //字段5：暂弃   秒杀id：能够秒杀cankill ---无需更新----暂时不启用，使用开始时间和结束时间临时算出来，后面再考虑如何使用
     */
    public void initSecKillInfoToRedis(){
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

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        initSecKillInfoToRedis();
    }
}
