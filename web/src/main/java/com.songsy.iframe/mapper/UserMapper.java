package com.songsy.iframe.mapper;



import com.songsy.iframe.core.persistence.provider.mapper.BaseCurdMapper;
import com.songsy.iframe.model.User;

/**
 * 用户
 * @author songshuiyang
 * @date 2017/11/28 20:12
 */
public interface UserMapper extends BaseCurdMapper<User,Integer> {
    User selectByPrimaryKey(Integer id);
}