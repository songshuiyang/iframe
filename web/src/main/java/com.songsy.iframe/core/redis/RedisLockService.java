package com.songsy.iframe.core.redis;

/**
 * Redis 锁处理
 * @author songsy
 * @date 2019/1/22 10:43
 */
public interface RedisLockService {

    /**
     * 加锁
     *
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return
     */
    boolean lock(String lockKey, String requestId);

    /**
     * 加锁
     *
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 锁的有效时间(s)
     * @return
     */
    boolean lock(String lockKey, String requestId, long expireTime);

    /**
     * 解锁
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return
     */
    boolean unlock(String lockKey, String requestId);

}
