package com.songsy.iframe.core.persistence.datasource.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定数据源
 * @author songsy
 * @Date 2018/11/7 17:33
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BindingDataSources {

    String value() default "master";

}
