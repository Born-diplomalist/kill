package com.born.secKill.server.service;

import com.born.secKill.model.entity.User;

/**
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-03-29 14:07:24
 */
public interface IUserService {
    /**
     * 获取指定用户名的用户信息
     * @return
     */
    User getUserByUserName(String userName);

    User getUserByUserId(Integer userId);

    /**
     * 注册
     * @return
     */
    User regedit(User user);

}
