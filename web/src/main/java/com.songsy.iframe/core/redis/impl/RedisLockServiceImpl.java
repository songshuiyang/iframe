package com.songsy.iframe.core.redis.impl;

import com.songsy.iframe.core.redis.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis 锁处理
 * @author songsy
 * @date 2019/1/22 10:44
 */
@Slf4j
@Service
public class RedisLockServiceImpl implements RedisLockService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 锁默认过期时间 5分钟
     */
    private static final long  DEFAULT_LOCK_EXPIRE_TIME = 300;

    /**
     * 将key 的值设为value ，当且仅当key 不存在，等效于 SETNX。
     */
    private static final String NX = "NX";

    /**
     * seconds — 以秒为单位设置 key 的过期时间，等效于EXPIRE key seconds
     */
    private static final String EX = "EX";

    /**
     * 调用set后的返回值
     */
    private static final String OK = "OK";

    /**
     * 解锁的lua脚本
     */
    private static final String UNLOCK_LUA;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    @Override
    public boolean lock(String lockKey, String requestId) {
        return lock(lockKey, requestId, DEFAULT_LOCK_EXPIRE_TIME);
    }

    @Override
    public boolean lock(String lockKey, String requestId, long expireTime) {
        Assert.isTrue(StringUtils.isNotEmpty(lockKey), "key不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(requestId), "请求标识不能为空");
        String result = set(lockKey, requestId, expireTime);
        return OK.equalsIgnoreCase(result);
    }

    @Override
    public boolean unlock(String lockKey, String requestId) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                Long result = 0L;

                List<String> keys = new ArrayList<>();
                keys.add(lockKey);
                List<String> values = new ArrayList<>();
                values.add(requestId);

                // 集群模式
                if (nativeConnection instanceof JedisCluster) {
                    result = (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, keys, values);
                }

                // 单机模式
                if (nativeConnection instanceof Jedis) {
                    result = (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, keys, values);
                }

                if (result == 0) {
                    log.info("Redis分布式锁，解锁{}失败！解锁时间：{}", lockKey, System.currentTimeMillis());
                }
                return result == 1;
            }
        });
    }

    /**
     * 重写redisTemplate的set方法
     * <p>
     * 命令 SET resource-name anystring NX EX max-lock-time 是一种在 Redis 中实现锁的简单方法。
     * <p>
     * 客户端执行以上的命令：
     * <p>
     * 如果服务器返回 OK ，那么这个客户端获得锁。
     * 如果服务器返回 NIL ，那么客户端获取锁失败，可以在稍后再重试。
     *
     * @param key     锁的Key
     * @param value   锁里面的值
     * @param seconds 过去时间（秒）
     * @return
     */
    private String set(final String key, final String value, final long seconds) {
        Assert.isTrue(StringUtils.isNotEmpty(key), "key不能为空");
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                String result = null;
                // 集群模式
                if (nativeConnection instanceof JedisCluster) {
                    result = ((JedisCluster) nativeConnection).set(key, value, NX, EX, seconds);
                }
                // 单机模式
                if (nativeConnection instanceof Jedis) {
                    result = ((Jedis) nativeConnection).set(key, value, NX, EX, seconds);
                }

                log.info("获取锁{}的时间：{}", key, System.currentTimeMillis());

                return result;
            }
        });
    }

}
