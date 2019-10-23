package com.songsy.iframe.service.impl;


import com.songsy.iframe.core.persistence.datasource.annotation.MasterDataSource;
import com.songsy.iframe.core.persistence.provider.mapper.BaseCurdMapper;
import com.songsy.iframe.core.persistence.provider.service.AbstractBaseService;
import com.songsy.iframe.mapper.UserMapper;
import com.songsy.iframe.model.User;
import com.songsy.iframe.service.UserMasterDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 在类上加切换数据源注解
 *
 * @author songshuiyang
 * @date 2018/10/28 10:13
 */
@Service
@MasterDataSource
public class UserMasterServiceImpl extends AbstractBaseService<User, Integer> implements UserMasterDataService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public BaseCurdMapper<User, Integer> getRepository() {
        return userMapper;
    }

    @Override
    public void updateMasterDatabase(User user) {
        userMapper.insert(user);
    }


}
