package com.songsy.iframe.core.persistence.provider.annotation;

import java.lang.annotation.*;

/**
 * @author songsy
 * @Date 2018/11/1 11:15
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogPrint {
    String value() default "";
}
