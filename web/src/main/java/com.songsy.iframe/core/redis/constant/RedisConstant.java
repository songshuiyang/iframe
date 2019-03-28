package com.songsy.iframe.core.redis.constant;

/**
 * @author songsy
 * @date 2019/3/28 17:20
 */
public interface RedisConstant {

    String REDIS_COMMON_PREFIX = "iframe_";

    String REDIS_LOCK_KEY_FREFIX = REDIS_COMMON_PREFIX + "redis_lock_";

    /**
     * 默认过期时间
     */
    long DEFAULT_EXPIRETIME_TIME = 300;

}
