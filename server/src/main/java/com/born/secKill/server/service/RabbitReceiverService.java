package com.born.secKill.server.service;/**
 * Created by Administrator on 2020/3/21.
 */

import com.born.secKill.model.dto.KillSuccessUserInfo;
import com.born.secKill.model.entity.OrderInfo;
import com.born.secKill.model.entity.SecKill;
import com.born.secKill.model.mapper.OrderInfoMapper;
import com.born.secKill.model.mapper.SeckillMapper;
import com.born.secKill.server.dto.MailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ接收消息服务
 * @Author:gyk
 * @Date: 2020/3/21 21:47
 **/
@Service
public class RabbitReceiverService {

    public static final Logger log= LoggerFactory.getLogger(RabbitReceiverService.class);

    @Autowired
    private MailService mailService;

    @Autowired
    private Environment env;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 秒杀异步邮件通知-接收消息，发送邮件
     */
    @RabbitListener(queues = {"${mq.kill.item.success.email.queue}"},containerFactory = "singleListenerContainer")//采用单一消费者Bean
    public void consumeEmailMsg(KillSuccessUserInfo info){
        try {
            log.info("秒杀异步邮件通知-接收消息:{}",info);

            //发送邮件测试测试
            //MailDto dto=new MailDto(env.getProperty("mail.kill.item.success.subject"),"这是测试内容",new String[]{info.getEmail()});
            //mailService.sendSimpleEmail(dto);

            //邮件内容和主题
            final String content=String.format(env.getProperty("mail.kill.item.success.content"),info.getItemName(),info.getCode());
            MailDto dto=new MailDto(env.getProperty("mail.kill.item.success.subject"),content,new String[]{info.getEmail()});
            mailService.sendHTMLMail(dto);

        }catch (Exception e){
            log.error("秒杀异步邮件通知-接收消息-发生异常：",e.fillInStackTrace());
            e.printStackTrace();
        }
    }




    /**
     * 用户秒杀成功后超时未支付-监听者，处理死信队列
     * @param info
     */
    @RabbitListener(queues = {"${mq.kill.item.success.kill.dead.real.queue}"},containerFactory = "singleListenerContainer")
    public void consumeExpireOrder(KillSuccessUserInfo info){
        try {
            log.info("用户秒杀成功后超时未支付-监听者-接收消息:{}",info);

            if (info!=null){
                OrderInfo orderInfo= orderInfoMapper.selectByPrimaryKey(info.getCode());
                //针对0未支付状态的订单做判断，将其设置为-1失效，1已付款和2已取消是用户行为不做处理
                if (orderInfo!=null && orderInfo.getStatusCode().intValue()==0){
                    orderInfoMapper.expireOrder(info.getCode());
                    //订单失效后，应该回补mysql和redis的库存
                    SecKill secKill = new SecKill();
                    secKill.setTotal(secKill.getTotal()+1);
                    if (seckillMapper.updateKill(secKill)>0){
                        redisTemplate.opsForHash().increment(orderInfo.getKillId().toString(),orderInfo.getUserId(),1);
                    }
                }
            }
        }catch (Exception e){
            log.error("用户秒杀成功后超时未支付-监听者--死信队列-发生异常：",e.fillInStackTrace());
        }
    }
}












