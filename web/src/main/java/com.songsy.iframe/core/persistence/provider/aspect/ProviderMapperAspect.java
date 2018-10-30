package com.songsy.iframe.core.persistence.provider.aspect;

import com.google.common.collect.Maps;
import com.songsy.iframe.core.persistence.provider.MyBatisProviderContext;
import com.songsy.iframe.core.persistence.provider.ProviderContext;
import com.songsy.iframe.core.persistence.provider.mapper.BaseMapper;
import com.songsy.iframe.core.persistence.provider.utils.ReflectionUtils;
import org.apache.ibatis.binding.MapperProxy;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author songshuiyang
 * @date 2018/10/28 12:17
 */
@Aspect
@Component
@Order(1)
public class ProviderMapperAspect {
    private static Map<String, ProviderContext> providerContextMap = Maps.newHashMap();

    private final static Logger logger = LoggerFactory.getLogger(ProviderMapperAspect.class);

    @Pointcut("this(com.songsy.iframe.core.persistence.provider.mapper.BaseCurdMapper)")
    public void repositoryExecution() {

    }

    @Before("repositoryExecution()")
    public void setDynamicDataSource(JoinPoint point) {
        Class entityClass = null;
        Object target = point.getTarget();
        if (BaseMapper.class.isAssignableFrom(target.getClass())) {
            MapperProxy mapperProxy = (MapperProxy) Proxy.getInvocationHandler(target);
            Class mapperInterface = (Class) ReflectionUtils.getFieldValue(mapperProxy, "mapperInterface");
            ParameterizedType parameterizedType = (ParameterizedType) mapperInterface.getGenericInterfaces()[0];
            Type[] types = parameterizedType.getActualTypeArguments();
            try {
                entityClass = Class.forName(types[0].getTypeName());
                if (!providerContextMap.containsKey(entityClass.getName())) {
                    ProviderContext providerContext = new ProviderContext();
                    providerContext.setEntityClass(entityClass);
                    providerContext.setIdClass(Class.forName(types[1].getTypeName()));
                    providerContextMap.put(entityClass.getName(), providerContext);
                }
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage());
            }
        }
        if (entityClass != null) {
            MyBatisProviderContext.get().setProviderContext(providerContextMap.get(entityClass.getName()));
        }
    }

    @After("repositoryExecution()")
    public void clearProviderMapper(JoinPoint point) {
        MyBatisProviderContext.get().remove();
    }
}
