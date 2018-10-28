package com.songsy.iframe.core.persistence.provider.annotation;

import java.util.UUID;

/**
 * 定义主键生成策略的类型，JPA提供的四种标准用法为TABLE,SEQUENCE,IDENTITY,AUTO 在这里新加了一个CUSTOM 的生成策略
 * @author songshuiyang
 * @date 2018/10/28 10:26
 */
public enum GenerationType {
    /**
     * 使用一个特定的数据库表格来保存主键。
     */
    TABLE,
    /**
     * 根据底层数据库的序列来生成主键，条件是数据库支持序列。
     */
    SEQUENCE,
    /**
     * 主键由数据库自动生成（主要是自动增长型）
     */
    IDENTITY,
    /**
     * 主键由程序控制(也是默认的,在指定主键时，如果不指定主键生成策略，默认为AUTO)
     */
    AUTO,
    /**
     * 自定义
     */
    CUSTOM;
}
