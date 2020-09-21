package com.born.secKill.server.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 秒杀参数封装
 * @Author:gyk
 * @Date: 2020/3/17 22:18
 **/
@Data
@ToString
public class KillDto implements Serializable{

    @NotNull
    private Integer killId;

    private Integer userId;
}