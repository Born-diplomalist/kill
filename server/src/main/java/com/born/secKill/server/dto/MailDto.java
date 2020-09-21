package com.born.secKill.server.dto;/**
 * Created by Administrator on 2020/3/22.
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author:gyk
 * @Date: 2020/3/22 10:11
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MailDto implements Serializable{
    //邮件主题
    private String subject;
    //邮件内容
    private String content;
    //接收人
    private String[] tos;
}