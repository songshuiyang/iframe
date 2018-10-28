package com.songsy.iframe.service.impl;


import com.songsy.iframe.mapper.UserMapper;
import com.songsy.iframe.model.User;
import com.songsy.iframe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author songshuiyang
 * @date 2018/10/28 10:13
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }
}
