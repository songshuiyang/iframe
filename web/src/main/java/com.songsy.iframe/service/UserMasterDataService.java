package com.songsy.iframe.service;


import com.songsy.iframe.core.persistence.provider.service.BaseService;
import com.songsy.iframe.model.User;

/**
 * @author songshuiyang
 * @date 2018/10/28 10:13
 */
public interface UserMasterDataService extends BaseService<User, Integer> {

    void updateMasterDatabase(User user) ;
}
