package com.born.secKill.server.service.impl;/**
 * Created by Administrator on 2020/3/17.
 */

import com.born.secKill.model.entity.OrderInfo;
import com.born.secKill.model.entity.SecKill;
import com.born.secKill.model.mapper.SeckillMapper;
import com.born.secKill.model.mapper.OrderInfoMapper;
import com.born.secKill.server.common.ConstantClass;
import com.born.secKill.server.service.ISecKillService;
import com.born.secKill.server.service.RabbitSenderService;
import com.born.secKill.server.enums.SysConstant;
import com.born.secKill.server.utils.DateUtil;
import com.born.secKill.server.utils.JsonBean;
import com.born.secKill.server.utils.SnowFlake;
import com.born.secKill.server.utils.StatusCode;
import org.joda.time.DateTime;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 秒杀业务实现类
 *
 * @Author:gyk
 * @Date: 2020/3/17 22:21
 **/
@Service
public class SecKillServiceImpl implements ISecKillService {

    private static final Logger log = LoggerFactory.getLogger(SecKillServiceImpl.class);

    private SnowFlake snowFlake = new SnowFlake(2, 3);

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private RabbitSenderService rabbitSenderService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 获取所有存在的秒杀商品列表
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<SecKill> listAllKill() {
        return seckillMapper.listAllKill();
    }


    /**
     * 获取所有启用的秒杀商品列表
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<SecKill> listActiveKill() {
        return seckillMapper.listActiveKill();
    }

    /**
     * 获取指定秒杀商品详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public SecKill getKillDetail(Integer id) throws Exception {
        return seckillMapper.selectKillById(id);
    }


    // 针对秒杀系统秒杀商品数量较少的特性，对于一些灵活的数据，直接使用redis操作，定时更新数据到mysql即可
    //     * 对于那些固定不变的信息，比如秒杀id对应的商品id等信息，再去mysql查
    //     * 死信队列的回补库存，也要同时对redis对应值回补
    //弊端： redis异常宕机，或者在定时更新MySQL之前程序终止，mysql的数据就不是最新的了，而下次开机时，redis的数据是以mysql作为依据的，因此，那部分没更新的数据就彻底丢失了
    //弊端处理： 初始化Redis数据时，先读取redis的持久化文件，恢复上次未保存到数据库的数据，再和mysql数据合并放到redis，然后下一次定时任务就会把这些数据都更新到mysql
    //处理后的弊端：多个系统模块之间的通信都是查MySQL数据库，秒杀系统以外的其他系统需要用到秒杀数据时，比如将秒杀剩余的库存转回正常购买商品的库存中时


    /**
     * 商品秒杀核心业务逻辑的处理
     * 使用Redisson做分布式锁
     * 使用redis做缓存
     * <p>
     * 字段1：  秒杀id：用户id：秒杀数量
     * 字段2：  秒杀id：库存
     * 字段3：  秒杀id：开始时间
     * 字段4：  秒杀id：结束时间
     * <p>
     * <p>
     * @param killId 秒杀id
     */
    @Override
    public JsonBean<Object> killItem(Integer killId, Integer userId) throws Exception {
        HashOperations opsForHash = redisTemplate.opsForHash();
        String killIdStr = killId.toString();
        String userIdStr = userId.toString();

        //TODO:如果取某个值做判断时Redis中没有对应数据，应去数据库中查询，否则参数就是和NULL比较

        //判断是否在秒杀时间段内
        long startTime = Long.parseLong((String) opsForHash.get(killIdStr, "start_time"));
        long endTime = Long.parseLong(opsForHash.get(killIdStr, "end_time").toString());
        long currentTime = new Date().getTime();
        if (startTime > currentTime || endTime < currentTime) {
            return new JsonBean<>(StatusCode.SECKILL_OUT_OF_TIME);
        }
        //判断用户是否已抢购过
        if (ConstantClass.USER_HAS_SECKILLED.equals(opsForHash.get(killIdStr, userIdStr))) {
            return new JsonBean<>(StatusCode.USER_HAS_SECKILL);
        }
        //判断库存是否充足
        Integer total = Integer.valueOf(opsForHash.get(killIdStr, "total").toString());
        if (total <= 0) {
            return new JsonBean<>(StatusCode.SECKILL_STOCK_NOT_ENOUGH);
        }

        //StringBuilder线程不安全，String浪费空间
        final String lockKey = new StringBuffer().append(killIdStr).append(userIdStr).append("-RedissonLock").toString();

        //redisson的分布式锁
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean cacheRes = lock.tryLock(30, 10, TimeUnit.SECONDS);
            if (cacheRes) {
                //TODO:核心业务逻辑的处理

                //判断当前用户是否已经抢购过当前商品-
                if (orderInfoMapper.countByKillUserId(killId, userId) <= 0) {
                    SecKill kill = seckillMapper.selectKillById(killId);
                    //判断是否可以被秒杀 对象不为null canKill==1  total>0?
                    if (kill != null && 1 == kill.getCanKill() && kill.getTotal() > 0) {
                        //扣减mysql库存
                        int res = seckillMapper.updateKillStock(killId);
                        if (res > 0) {
                            //更新redis中购买信息
                            opsForHash.put(killIdStr, userIdStr, ConstantClass.USER_HAS_SECKILLED);
                            //更新redis中的库存
                            opsForHash.increment(killIdStr, "total", -1);
                            //扣减库存成功，生成订单，异步发送邮件和异步将订单入死信队列
                            commonRecordKillSuccessInfo(kill, userId);
                            return new JsonBean<Object>(StatusCode.SECKILL_SUCCESS);
                        }
                    }
                } else {
                    throw new Exception("redisson-您已经抢购过该商品了!");
                }
            }
        } finally {
            lock.unlock();
            //lock.forceUnlock();//强制释放
        }
        return new JsonBean<Object>(StatusCode.FAIL);
    }

    @Override
    public Integer addKill(SecKill kill) {
        return seckillMapper.insertKill(kill);
    }

    @Override
    public Integer modifyKill(SecKill kill) {
        return seckillMapper.updateKill(kill);
    }

    /**
     * 更改秒杀商品激活状态
     */
    @Override
    public Integer changeActiveStatus(Integer id) {
        SecKill kill = seckillMapper.selectKillById(id);
        if (null == kill) {
            //TODO: Service不应抛出异常，应该将错误消息存起来交给Controller处理，适合返回JsonBean
            log.error("id为{}的秒杀商品不存在", id);
            throw new RuntimeException("id为" + id + "的秒杀商品不存在");
        }
        if (ConstantClass.SECKILL_ACTIVE.equals(kill.getIsActive())) {
            kill.setIsActive(ConstantClass.SECKILL_FORBIDDEN);
        } else if (ConstantClass.SECKILL_FORBIDDEN.equals(kill.getIsActive())) {
            kill.setIsActive(ConstantClass.SECKILL_ACTIVE);
        } else {
            log.error("id为{}的秒杀商品启用状态异常", id);
            throw new RuntimeException("id为" + id + "的秒杀商品启用状态异常");
        }
        return seckillMapper.updateKill(kill);
    }


    /**
     * 记录用户秒杀成功后生成的订单-并进行异步邮件消息的通知
     */
    private void commonRecordKillSuccessInfo(SecKill kill, Integer userId) throws Exception {

        OrderInfo entity = new OrderInfo();
        String orderNo = String.valueOf(snowFlake.nextId());

        //entity.setCode(RandomUtil.generateOrderCode());   //传统时间戳+N位随机数
        entity.setCode(orderNo); //雪花算法
        entity.setItemId(kill.getItemId());
        entity.setKillId(kill.getId());
        entity.setUserId(userId.toString());
        entity.setStatusCode(SysConstant.OrderStatus.SuccessNotPayed.getCode().byteValue());
        entity.setCreateTime(DateTime.now().toDate());
        if (orderInfoMapper.countByKillUserId(kill.getId(), userId) <= 0) {
            int res = orderInfoMapper.insertSelective(entity);

            if (res > 0) {
                //进行异步邮件消息的通知=rabbitmq+mail
                rabbitSenderService.sendKillSuccessEmailMsg(orderNo);

                //入死信队列，用于 “失效” 超过指定的TTL时间时仍然未支付的订单
                rabbitSenderService.sendKillSuccessOrderExpireMsg(orderNo);
            }
        }
    }


}

