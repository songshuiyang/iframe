package com.songsy.iframe.test;

import com.songsy.iframe.Application;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author songsy
 * @Date 2018/10/31 17:56
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class BaseTest {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

}
