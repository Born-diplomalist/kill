package com.born.secKill.server.config;/**
 * Created by Administrator on 2020/3/29.
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

/**
 * 定时任务多线程处理的通用化配置
 * @Author:gyk
 * @Date: 2020/3/29 21:45
 **/
@Configuration
public class SchedulerConfig implements SchedulingConfigurer{

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //分配10个线程用于处理定时任务
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));
    }
}