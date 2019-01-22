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

    /**
     * 加锁
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return
     */
    @Override
    public boolean lock(String lockKey, String requestId) {
        return lock(lockKey, requestId, DEFAULT_LOCK_EXPIRE_TIME);
    }

    /**
     * 加锁
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 锁的有效时间(s)
     * @return
     */
    @Override
    public boolean lock(String lockKey, String requestId, long expireTime) {
        Assert.isTrue(StringUtils.isNotEmpty(lockKey), "key不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(requestId), "请求标识不能为空");
        String result = set(lockKey, requestId, expireTime);
        return OK.equalsIgnoreCase(result);
    }

    /**
     * 解锁
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return
     */
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
                    log.info("<<< Redis锁 {} 解锁失败！lockkey: {}, 处理时间：{}",requestId, lockKey, System.currentTimeMillis());
                } else {
                    log.info("<<< Redis锁 {} 解锁成功！lockkey: {}, 处理时间：{}",requestId, lockKey, System.currentTimeMillis());
                }
                return result == 1;
            }
        });
    }

    /**
     * 重写redisTemplate的set方法
     * <p>
     * 命令 SET resource-name anystring NX EX max-lock-time 是一种在 Redis 中实现锁的简单方法。
     *
     * NX，意思是SET IF NOT EXIST，即当key不存在时，我们进行set操作；若key已经存在，则不做任何操作
     * PX，意思是我们要给这个key加一个过期的设置
     * <p>
     *
     * @param lockKey   锁的Key
     * @param requestId 分布式锁要满足解铃还须系铃人，通过给value赋值为requestId，我们就知道这把锁是哪个请求加的了，在解锁的时候就可以有依据
     * @param seconds   过期时间（秒）
     * @return
     */
    private String set(final String lockKey, final String requestId, final long seconds) {
        Assert.isTrue(StringUtils.isNotEmpty(lockKey), "key不能为空");
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                String result = null;
                // 集群模式
                if (nativeConnection instanceof JedisCluster) {
                    result = ((JedisCluster) nativeConnection).set(lockKey, requestId, NX, EX, seconds);
                }
                // 单机模式
                if (nativeConnection instanceof Jedis) {
                    result = ((Jedis) nativeConnection).set(lockKey, requestId, NX, EX, seconds);
                }
                if (StringUtils.isBlank(result)) {
                    log.info(">>> Redis锁 {} 加锁失败！lockkey: {}, 处理时间：{}", requestId, lockKey, System.currentTimeMillis());
                } else {
                    log.info(">>> Redis锁 {} 加锁成功！lockkey: {}, 处理时间：{}", requestId, lockKey, System.currentTimeMillis());
                }
                return result;
            }
        });
    }
}
