package com.songsy.iframe.service.impl;


import com.songsy.iframe.core.persistence.provider.mapper.BaseCurdMapper;
import com.songsy.iframe.core.persistence.provider.service.AbstractBaseService;
import com.songsy.iframe.mapper.slave.UserSlaveMapper;
import com.songsy.iframe.model.User;
import com.songsy.iframe.service.UserSlaveDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author songshuiyang
 * @date 2018/10/28 10:13
 */
@Service
public class UserSlaveServiceImpl extends AbstractBaseService<User, Integer> implements UserSlaveDataService {

    @Autowired
    private UserSlaveMapper userSlaveMapper;

    @Override
    public BaseCurdMapper<User, Integer> getRepository() {
        return userSlaveMapper;
    }

    @Override
    public void updateSlaveDatabase(User user) {
        userSlaveMapper.insert(user);
    }

}
