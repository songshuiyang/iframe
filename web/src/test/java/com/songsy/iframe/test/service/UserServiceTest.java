package com.songsy.iframe.test.service;

import com.songsy.iframe.model.User;
import com.songsy.iframe.service.UserService;
import com.songsy.iframe.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author songsy
 * @Date 2018/10/31 18:00
 */
public class UserServiceTest extends BaseTest {

    @Autowired
    UserService userService;

    @Test
    public void insertUser () {
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
    public void updateUser1 () {
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
    public void updateUser2 () {
        User user = userService.findAll().get(0);
        User userDb = new User();
        userDb.setId(user.getId());
        userDb.setVersion(user.getVersion());
        userDb.setUsername("测试乐观锁111");
        userService.saveSelective(userDb);
    }

    @Test
    public void selectByPrimaryKey () {
        System.out.println(userService.selectByPrimaryKey(1));
    }

}
