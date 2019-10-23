package com.songsy.iframe.core.persistence.datasource.aspect;

import com.songsy.iframe.core.persistence.datasource.DynamicDataSourceHolder;
import com.songsy.iframe.core.persistence.datasource.annotation.BindingDataSources;
import com.songsy.iframe.core.persistence.datasource.annotation.MasterDataSource;
import com.songsy.iframe.core.persistence.datasource.annotation.SlaveDataSource;
import com.songsy.iframe.core.persistence.datasource.common.DataSouceConstant;
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 配置数据源切面
 * @author songsy
 * @Date 2018/11/7 17:35
 */
@Aspect
@Order(-9999)// 保证该AOP在@Transactional之前执行
@Component
public class DynamicDataSourceAspect {

    private final static Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);
//
//    @Pointcut("@annotation(com.songsy.iframe.core.persistence.datasource.annotation.BindingDataSources)")
//    public void pointcut() {
//    }

    @Pointcut("execution(* com.songsy.iframe.service.*.*(..))")
    public void serviceExecution() {
    }

    @Before("serviceExecution()")
    public void setDynamicDataSource(JoinPoint point) {
        Object target = point.getTarget();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        Annotation annotation = method.getAnnotation(BindingDataSources.class);
        Annotation [] annotations = method.getDeclaredAnnotations();

        // 可以放在方法上，也可以放在类上
        if (method.isAnnotationPresent(SlaveDataSource.class) || target.getClass().isAnnotationPresent(SlaveDataSource.class)) {
            DynamicDataSourceHolder.setDataSource(DataSouceConstant.SLAVE_DATA_SOURCE_PREFIX);
            logger.debug("数据源：[{}] 类名: {} 方法名: {}", DynamicDataSourceHolder.getDataSource(), target.getClass().getCanonicalName(), method.getName());
        } else {
            DynamicDataSourceHolder.setDataSource(DataSouceConstant.MASTER_DATA_SOURCE_PREFIX);
            logger.debug("数据源：[{}] 类名: {} 方法名: {}", DynamicDataSourceHolder.getDataSource(), target.getClass().getCanonicalName(), method.getName());
        }
    }

    @After("serviceExecution()")
    public void clearDynamicDataSource(JoinPoint point) {
        DynamicDataSourceHolder.removeDataSource();
    }
}
