package com.songsy.iframe.test.service;

import com.songsy.iframe.core.persistence.provider.Page;
import com.songsy.iframe.model.User;
import com.songsy.iframe.service.UserService;
import com.songsy.iframe.test.BaseTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author songsy
 * @Date 2018/10/31 18:00
 */
public class UserServiceTest extends BaseTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    /**
     * 测试切换数据源
     */
    @Test
    public void findAll() {
        logger.info(userService.findAll().toString());
    }

    /**
     * 测试
     */
    @Test
    public void updateAllUserTest1() {
        User user = new User();
//        user.setId(48);
        user.setUsername("songsy20191023");
        user.setAddress("广东深圳");
        user.setAge(88);
        user.setEmail("1459074711@qq.com");
        user.setHeadPortrait("头像");
        user.setNickname("宋某某");
        user.setPassword("root");
        user.setSex(1);
        user.setVersion(new Long(9999));
        userService.updateMasterDatabase(user);
        userService.updateSlaveDatabase(user);
    }

    /**
     * 测试
     */
    @Test
    public void updateAllUserTest2() {
        User user = new User();
//        user.setId(48);
        user.setUsername("songsy20191023");
        user.setAddress("广东深圳");
        user.setAge(88);
        user.setEmail("1459074711@qq.com");
        user.setHeadPortrait("头像");
        user.setNickname("宋某某");
        user.setPassword("root");
        user.setSex(1);
        user.setVersion(new Long(9999));
        userService.updateAllUser(user);
    }

    @Test
    public void insertUser() {
        User user = new User();
        user.setUsername("songsy");
        user.setAddress("广东深圳");
        user.setAge(88);
        user.setEmail("1459074711@qq.com");
        user.setHeadPortrait("头像");
        user.setNickname("宋某");
        user.setPassword("root");
        user.setSex(1);
        userService.saveSelective(user);
    }

    @Test
    public void updateUser1() {
        User user = new User();
        user.setId(48);
        user.setUsername("songsy");
        user.setAddress("广东深圳");
        user.setAge(88);
        user.setEmail("1459074711@qq.com");
        user.setHeadPortrait("头像");
        user.setNickname("宋某某");
        user.setPassword("root");
        user.setSex(1);
        user.setVersion(1l);
        userService.saveSelective(user);
    }

    @Test
    public void updateUser2() {
        User user = userService.findAll().get(0);
        User userDb = new User();
        userDb.setId(user.getId());
        userDb.setVersion(user.getVersion());
        userDb.setUsername("测试乐观锁111");
        userService.saveSelective(userDb);
    }

    @Test
    public void updateNull() {
        User user = userService.findById(50);
        User userDb = new User();
        userDb.setId(user.getId());
        userDb.setVersion(user.getVersion());
        userDb.setUsername("测试updateNull");
        userService.updateNull(userDb);
    }

    @Test
    public void deleteOne() {
        userService.deleteOne(48);
    }

    @Test
    public void logicDeleteOne() {
        userService.logicDeleteOne(49);
    }

    @Test
    public void findAutoByPage1() {
        Page<User> userPage = new Page<User>(0);
        userPage.setParams("username", "ssy");
        System.out.println(userService.findAutoByPage(userPage));
    }
}
