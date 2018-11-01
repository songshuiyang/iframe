package com.songsy.iframe.test.persistence;

import com.songsy.iframe.core.persistence.provider.CrudProvider;
import com.songsy.iframe.mapper.UserMapper;
import com.songsy.iframe.model.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author songsy
 * @Date 2018/10/31 16:23
 */
public class MybatisProviderTest {

    private static Logger logger = LoggerFactory.getLogger(CrudProvider.class);

    @Autowired
    UserMapper userMapper;


    @Test
    public void findAllTest() {
        userMapper.findAll();
    }
    @Test
    public void findByIdTest() {
        User user = userMapper.findById(1);
        logger.info(user.toString());
    }

}
