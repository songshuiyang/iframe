package com.songsy.iframe.test.core.redis;

import com.songsy.iframe.core.redis.RedisLockService;
import com.songsy.iframe.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * @author songsy
 * @date 2019/1/22 10:59
 */
public class RedisLockServiceTest extends BaseTest {

    @Autowired
    RedisLockService redisLockService;


    @Test
    public void redisLockTest() {
        redisLockService.lock("shop_app_lock_key", UUID.randomUUID().toString());
    }
}
