package com.songsy.iframe.core.persistence.provider.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主键生成
 * @author songshuiyang
 * @date 2018/10/28 10:26
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GeneratedValue {
    /**
     * 主键生成策略
     * @return
     */
    GenerationType strategy() default GenerationType.IDENTITY;
}
