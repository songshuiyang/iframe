package com.songsy.iframe.test.spring;

import com.songsy.iframe.model.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @author songsy
 * @date 2019/5/14 15:48
 */
public class SpringTest {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        User user = applicationContext.getBean(User.class);
        List list = (List)applicationContext.getBean("userlist");
        // 报错 List list1 = (List)applicationContext.getBean(List.class);
        System.out.println(user);
    }
}
