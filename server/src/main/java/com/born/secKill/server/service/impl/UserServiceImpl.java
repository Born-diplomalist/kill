package com.born.secKill.server.service.impl;

import com.born.secKill.model.entity.User;
import com.born.secKill.model.mapper.UserMapper;
import com.born.secKill.server.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 用户
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-03-29 14:07:37
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取指定用户名的用户信息
     */
    @Override
    public User getUserByUserName(String userName) {
        return userMapper.selectByUserName(userName);
    }

    @Override
    public User getUserByUserId(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    /**
     * 注册
     */
    @Override
    public User regedit(User user) {

        int i = userMapper.insert(user);
        if (i>0) {return userMapper.selectByUserName(user.getUserName());}
        return null;
    }
}
