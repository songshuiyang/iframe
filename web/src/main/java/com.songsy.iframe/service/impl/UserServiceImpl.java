package com.songsy.iframe.service.impl;


import com.songsy.iframe.model.User;
import com.songsy.iframe.service.UserMasterDataService;
import com.songsy.iframe.service.UserService;
import com.songsy.iframe.service.UserSlaveDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author songshuiyang
 * @date 2018/10/28 10:13
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMasterDataService userMasterService;

    @Autowired
    private UserSlaveDataService userSlaveDataService;

    @Override
    @Transactional
    public void updateAllUser(User user) {
        userMasterService.updateMasterDatabase(user);
        // 触发事务回滚
        System.out.println(1/0);
        userSlaveDataService.updateSlaveDatabase(user);
    }

}
