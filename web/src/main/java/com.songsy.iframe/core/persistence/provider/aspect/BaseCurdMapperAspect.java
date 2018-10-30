package com.songsy.iframe.core.persistence.provider.aspect;

import com.google.common.collect.Maps;
import com.songsy.iframe.core.persistence.provider.mapper.BaseCurdMapper;
import com.songsy.iframe.core.persistence.provider.threadlocal.EntityProperty;
import com.songsy.iframe.core.persistence.provider.threadlocal.EntityThreadLocal;
import com.songsy.iframe.core.persistence.provider.utils.ReflectionUtils;
import org.apache.ibatis.binding.MapperProxy;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * BaseCurdMapper接口AOP，用于获取实体类属性
 *
 * @author songshuiyang
 * @date 2018/10/30 21:44
 */
@Aspect
@Component
public class BaseCurdMapperAspect {

    private final static Logger logger = LoggerFactory.getLogger(BaseCurdMapperAspect.class);

    /**
     * 缓存实体类属性
     * key: 实体类类型
     * value: 实体类属性对象
     */
    private static Map<String, EntityProperty> entityPropertyMap = Maps.newHashMap();

    /**
     * 定义切点
     * Spring Aop是基于代理的，生成的bean也是一个代理对象，this就是这个代理对象，
     * 当这个对象可以转换为指定的类型时，对应的切入点就是它了，Spring Aop将生效。
     */
    @Pointcut("this(com.songsy.iframe.core.persistence.provider.mapper.BaseCurdMapper)")
    public void pointcut() {
    }

    /**
     * 前置增强：获取BaseCurdMapper接口 泛型属性，并设置到ThreadLocal中
     * @param point
     */
    @Before("pointcut()")
    public void before(JoinPoint point) {
        Class entityClass = null;
        Class entityIdClass = null;
        Object target= point.getTarget();
        // 是否继承 BaseCurdMapper 接口
        if (BaseCurdMapper.class.isAssignableFrom(target.getClass())) {
            // 获取Mybatis代理类对象
            MapperProxy mapperProxy = (MapperProxy) Proxy.getInvocationHandler(target);
            Class mapperInterface = (Class) ReflectionUtils.getFieldValue(mapperProxy, "mapperInterface");
            // 获取接口泛型对象
            ParameterizedType parameterizedType = (ParameterizedType) mapperInterface.getGenericInterfaces()[0];
            Type[] types = parameterizedType.getActualTypeArguments();
            if (types.length != 2) {
                logger.error("parameterizedType type length error");
            }
            try {
                entityClass = Class.forName(types[0].getTypeName());
                entityIdClass = Class.forName(types[1].getTypeName());
                // 如果不存在则加入到entityPropertyMap缓存中
                if (!entityPropertyMap.containsKey(entityClass.getName())) {
                    EntityProperty entityProperty = new EntityProperty(entityClass, entityIdClass);
                    entityPropertyMap.put(entityClass.getTypeName(),entityProperty);
                }
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage());
            }
        }
        // 设置ThreadLocal
        if (null != entityClass) {
            EntityThreadLocal.set(entityPropertyMap.get(entityClass.getName()));
        }

    }

    /**
     * 后置增强：清除 threadLocal 防止内存泄漏
     * @param point
     */
    @After("pointcut()")
    public void after(JoinPoint point) {
        EntityThreadLocal.remove();
    }

}
