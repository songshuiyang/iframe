package com.songsy.iframe.mapper;



import com.songsy.iframe.core.persistence.provider.mapper.BaseMapper;
import com.songsy.iframe.model.User;

import java.util.List;
import java.util.Set;

/**
 * 用户
 * @author songshuiyang
 * @date 2017/11/28 20:12
 */
public interface UserMapper extends BaseMapper<User,Integer> {

    List<User> findAll();

}