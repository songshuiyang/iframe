package com.songsy.iframe.core.persistence.datasource.aspect;

import com.songsy.iframe.core.persistence.datasource.DynamicDataSourceHolder;
import com.songsy.iframe.core.persistence.datasource.annotation.BindingDataSources;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 配置数据源切面
 * @author songsy
 * @Date 2018/11/7 17:35
 */
@Aspect
@Order(-1)// 保证该AOP在@Transactional之前执行
@Component
public class DynamicDataSourceAspect {

    private final static Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    @Pointcut("@annotation(com.songsy.iframe.core.persistence.datasource.annotation.BindingDataSources)")
    public void pointcut() {
    }

    @Before("pointcut() && @annotation(bindingDataSources)")
    public void setDynamicDataSource(JoinPoint point, BindingDataSources bindingDataSources) {
        Object target = point.getTarget();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        logger.debug("切换数据源:  类名 - {}", target.getClass().getCanonicalName());
        logger.debug("切换数据源: 方法名 - {}", method.getName());
        String key = bindingDataSources.value();
        DynamicDataSourceHolder.setDataSource(key);
        logger.debug("切换数据源：[{}] 数据源切换成功.", DynamicDataSourceHolder.getDataSource());
    }

    @After("pointcut()")
    public void clearDynamicDataSource(JoinPoint point) {
        DynamicDataSourceHolder.removeDataSource();
    }
}
