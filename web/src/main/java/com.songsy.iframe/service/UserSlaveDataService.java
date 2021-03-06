package com.songsy.iframe.service;


import com.songsy.iframe.core.persistence.provider.service.BaseService;
import com.songsy.iframe.model.User;

/**
 * 在类上加切换数据源注解
 *
 * @author songshuiyang
 * @date 2018/10/28 10:13
 */
public interface UserSlaveDataService extends BaseService<User, Integer> {

    void updateSlaveDatabase(User user) ;

}
