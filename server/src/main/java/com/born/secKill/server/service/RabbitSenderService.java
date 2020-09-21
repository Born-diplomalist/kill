package com.born.secKill.server.service;/**
 * Created by Administrator on 2020/3/21.
 */

import com.born.secKill.model.dto.KillSuccessUserInfo;
import com.born.secKill.model.mapper.OrderInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ发送邮件服务
 *
 * 业务模块的服务化、功能模块的解耦
 *
 * @Author:gyk
 * @Date: 2020/3/21 21:47
 **/
@Service
public class RabbitSenderService {

    public static final Logger log= LoggerFactory.getLogger(RabbitSenderService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private OrderInfoMapper orderInfoMapper;



    //发送消息测试
    //Message message=MessageBuilder.withBody(orderNo.getBytes("UTF-8")).build();
    //rabbitTemplate.convertAndSend(message);
    /**
     * 秒杀成功异步发送用于邮件通知的消息
     * @param orderNo 订单编号
     */
    public void sendKillSuccessEmailMsg(String orderNo){
        log.info("秒杀成功异步发送邮件通知消息-准备发送消息：{}",orderNo);

        try {
            if (StringUtils.isNotBlank(orderNo)){
                KillSuccessUserInfo info= orderInfoMapper.selectDetailResultByCode(orderNo);
                if (info!=null){
                    //TODO:rabbitmq发送消息的逻辑
                    //将消息发送到交换机和路由中，会自动将消息路由到指定的队列
                    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                    rabbitTemplate.setExchange(env.getProperty("mq.kill.item.success.email.exchange"));
                    rabbitTemplate.setRoutingKey(env.getProperty("mq.kill.item.success.email.routing.key"));


                    //TODO：将info充当消息发送至队列
                    rabbitTemplate.convertAndSend(info, new MessagePostProcessor() {
                        @Override
                        public Message postProcessMessage(Message message) throws AmqpException {
                            MessageProperties messageProperties=message.getMessageProperties();
                            //配置消息持久化
                            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            //设定消息头
                            messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, KillSuccessUserInfo.class);
                            return message;
                        }
                    });
                }
            }
        }catch (Exception e){
            log.error("秒杀成功异步发送邮件通知消息-发生异常，消息为：{}",orderNo,e.fillInStackTrace());
        }
    }


    /**
     * 秒杀成功后生成抢购订单消息-发送信息入队列，等待着一定时间失效超时未支付的订单
     * 使用try-catch，直接处理异常，避免影响到主模块
     * @param orderCode
     */
    public void sendKillSuccessOrderExpireMsg(final String orderCode){
        try {
            if (StringUtils.isNotBlank(orderCode)){
                KillSuccessUserInfo info= orderInfoMapper.selectDetailResultByCode(orderCode);
                if (info!=null){
                    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                    //设置基本交换机和基本路由
                    rabbitTemplate.setExchange(env.getProperty("mq.kill.item.success.kill.dead.prod.exchange"));
                    rabbitTemplate.setRoutingKey(env.getProperty("mq.kill.item.success.kill.dead.prod.routing.key"));
                    rabbitTemplate.convertAndSend(info, new MessagePostProcessor() {
                        @Override
                        public Message postProcessMessage(Message message) throws AmqpException {
                            MessageProperties mp=message.getMessageProperties();
                            //设置消息传输模式为持久化
                            mp.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            //设置消息头
                            mp.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, KillSuccessUserInfo.class);
                            //TODO：动态设置TTL
                            mp.setExpiration(env.getProperty("mq.kill.item.success.kill.expire"));
                            return message;
                        }
                    });
                }
            }
        }catch (Exception e){
            log.error("秒杀成功后生成抢购订单消息-发送信息入死信队列，等待着一定时间失效超时未支付的订单-发生异常，消息为：{}",orderCode,e.fillInStackTrace());
        }
    }

}




























