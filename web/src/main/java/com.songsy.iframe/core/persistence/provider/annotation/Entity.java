package com.songsy.iframe.core.persistence.provider.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author songshuiyang
 * @date 2018/10/28 11:45
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Entity {

    String name() default "";

}
