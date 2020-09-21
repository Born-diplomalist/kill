package com.born.secKill.server.service;
/*
 * Created by Administrator on 2020/3/22.
 */

import com.born.secKill.server.dto.MailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * 邮件服务
 * @Author:gyk
 * @Date: 2020/3/22 10:09
 **/
@Service
@EnableAsync//使用异步发送
public class MailService {

    private static final Logger log= LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;


    /**
     * 发送简单文本文件
     * @param dto 邮件基本信息封装
     */
    @Async
    public void sendSimpleEmail(final MailDto dto){
        try {
            SimpleMailMessage message=new SimpleMailMessage();
            //发送方、接受者、邮件主题、邮件内容
            message.setFrom(env.getProperty("mail.send.from"));
            message.setTo(dto.getTos());
            message.setSubject(dto.getSubject());
            message.setText(dto.getContent());

            mailSender.send(message);
            log.info("发送简单文本文件-发送成功!");
        }catch (Exception e){
            log.error("发送简单文本文件-发生异常： ",e.fillInStackTrace());
        }
    }

    /**
     * 发送HTML格式可渲染邮件
     * @param dto 邮件基本信息封装
     */
    @Async
    public void sendHTMLMail(final MailDto dto){
        try {
            //多媒体类型邮件
            MimeMessage message=mailSender.createMimeMessage();
            MimeMessageHelper messageHelper=new MimeMessageHelper(message,true,"UTF-8");
            messageHelper.setFrom(env.getProperty("mail.send.from"));
            messageHelper.setTo(dto.getTos());
            messageHelper.setSubject(dto.getSubject());
            //使用html解析文本
            messageHelper.setText(dto.getContent(),true);

            mailSender.send(message);
            log.info("发送HTML邮件-发送成功!");
        }catch (Exception e){
            log.error("发送HTML邮件-发生异常： ",e.fillInStackTrace());
        }
    }
}































