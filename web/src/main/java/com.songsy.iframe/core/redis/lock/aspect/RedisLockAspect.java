package com.songsy.iframe.core.redis.lock.aspect;


import com.songsy.iframe.core.redis.RedisLockService;
import com.songsy.iframe.core.redis.lock.annotation.RedisLock;
import com.songsy.iframe.core.utils.IDGenerator;
import com.songsy.iframe.core.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

import static com.songsy.iframe.core.redis.constant.RedisConstant.REDIS_LOCK_KEY_FREFIX;


/**
 * Redis锁注解
 * @author songsy
 * @date 2019/3/28 17:08
 */
@Slf4j
@Aspect
@Configuration
public class RedisLockAspect {
    @Autowired
    private RedisLockService redisLockService;

    @Around(value = "@annotation(com.songsy.iframe.core.redis.lock.annotation.RedisLock)")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        boolean lockStatus = false;
        String lockKey = "";
        String requestId = "";
        try {
            MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
            Method method = methodSignature.getMethod();
            RedisLock redisLock = method.getAnnotation(RedisLock.class);
            Object[] arguments = pjp.getArgs();
            // 解析SpEL表达式
            String key = (String) parseSpel(redisLock.key(), method, arguments);
            String methodName = pjp.getSignature().getName();
            // 唯一标识
            lockKey = REDIS_LOCK_KEY_FREFIX + methodName + "_" + key;
            requestId = IDGenerator.getUUID();
            lockStatus = redisLockService.lock(lockKey, requestId);
        } catch (Exception e) {
            log.error("redisLockAspect 加锁失败, 直接执行方法",e);
            return pjp.proceed();
        }
        try {
            if (!lockStatus) {
                log.error("此方法正在处理中{}",lockKey);
                return ResponseUtil.error("该方法正在处理中");
            }
            result = pjp.proceed();
        } finally {
            if (lockStatus) {
                try {
                    redisLockService.unlock(lockKey, requestId);
                } catch (Exception e) {
                    log.error("redisLockAspect 解锁失败");
                }
            }
        }
        return result;
    }
    /**
     * 解析SpEL表达式
     * @param key    SpEL表达式
     * @param method 反射得到的方法
     * @param args   反射得到的方法参数
     * @return 解析后SpEL表达式对应的值
     */
    private Object parseSpel(String key, Method method, Object[] args) {
        if (StringUtils.isNotBlank(key)) {
            // 创建解析器
            ExpressionParser parser = new SpelExpressionParser();
            // 通过Spring的LocalVariableTableParameterNameDiscoverer获取方法参数名列表
            LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
            // 构造上下文
            EvaluationContext context = new StandardEvaluationContext();
            if (args.length == parameterNames.length) {
                for (int i = 0, len = args.length; i < len; i++) {
                    // 使用setVariable方法来注册自定义变量
                    context.setVariable(parameterNames[i], args[i]);
                }
            }
            return parser.parseExpression(key).getValue(context);
        }
        return "";
    }
}
