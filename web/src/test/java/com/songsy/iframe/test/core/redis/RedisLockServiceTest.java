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

    private String getRandomUUID () {
        return UUID.randomUUID().toString().replace("-","");
    }


    @Test
    public void redisLockTest() {
        boolean lock1 = redisLockService.lock("shop_app_lock_key", getRandomUUID());

        boolean lock2 = redisLockService.unlock("shop_app_lock_key", "5f62785af3504e9ca371caf9e1051d84");

        boolean lock3 = redisLockService.lock("shop_app_lock_key", getRandomUUID());

    }
}
