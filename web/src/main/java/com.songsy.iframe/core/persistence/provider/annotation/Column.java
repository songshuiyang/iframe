/*
 * Copyright (c) 2008, 2009, 2011 Oracle, Inc. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.  The Eclipse Public License is available
 * at http://www.eclipse.org/legal/epl-v10.html and the Eclipse Distribution License
 * is available at http://www.eclipse.org/org/documents/edl-v10.php.
 */
package com.songsy.iframe.core.persistence.provider.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 数据库列注解
 * @author songshuiyang
 * @date 2018/10/28 11:52
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Column {

    /**
     * 定义了被标注字段在数据库表中所对应字段的名称；
     * @return
     */
    String name() default "";

    /**
     * 表示该字段是否为唯一标识，默认为false。如果表中有一个字段需要唯一标识，则既可以使用该标记，也可以使用@Table标记中的@UniqueConstraint。
     * @return
     */
    boolean unique() default false;

    /**
     * 表示该字段是否可以为null值，默认为true。
     * @return
     */
    boolean nullable() default true;

    /**
     * 表示在使用“INSERT”脚本插入数据时，是否需要插入该字段的值。
     *
     */
    boolean insertable() default true;

    /**
     * 表示在使用“UPDATE”脚本插入数据时，是否需要更新该字段的值。insertable和updatable属性一般多用于只读的属性，例如主键和外键等。这些字段的值通常是自动生成的。
     *
     */
    boolean updatable() default true;

    /**
     * 表示创建表时，该字段创建的SQL语句，一般用于通过Entity生成表定义时使用。（也就是说，如果DB中表已经建好，该属性没有必要使用。）
     */
    String columnDefinition() default "";

    /**
     * 表示当映射多个表时，指定表的表中的字段。默认值为主表的表名。
     */
    String table() default "";

    /**
     * 表示字段的长度，当字段的类型为varchar时，该属性才有效，默认为255个字符。
     */
    int length() default 255;

    /**
     * precision表示数值的总长度
     */
    int precision() default 0;

    /**
     * scale表示小数点所占的位数。
     */
    int scale() default 0;
}
