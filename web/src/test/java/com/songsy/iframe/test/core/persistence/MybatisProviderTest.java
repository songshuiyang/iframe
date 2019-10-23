package com.songsy.iframe.test.core.persistence;

import com.songsy.iframe.core.persistence.provider.CrudProvider;
import com.songsy.iframe.mapper.slave.UserSlaveMapper;
import com.songsy.iframe.model.User;
import com.songsy.iframe.test.BaseTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author songsy
 * @Date 2018/10/31 16:23
 */
public class MybatisProviderTest extends BaseTest {

    private static Logger logger = LoggerFactory.getLogger(CrudProvider.class);

    @Autowired
    UserSlaveMapper userSlaveMapper;


    @Test
    public void findAllTest() {
        userSlaveMapper.findAll();
    }
    @Test
    public void findByIdTest() {
        User user = userSlaveMapper.findById(1);
        logger.info(user.toString());
    }

}
