package com.songsy.iframe.core.persistence.provider.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主键标识注解
 * @author songshuiyang
 * @date 2018/10/28 10:23
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
    /**
     * 标识id类型 解决泛型 T 类型，运行时被擦除为 Object
     * @return
     */
    Class type() default String.class;
}
