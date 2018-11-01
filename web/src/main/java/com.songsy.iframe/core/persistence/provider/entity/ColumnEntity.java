package com.songsy.iframe.core.persistence.provider.entity;

import lombok.Data;

import java.lang.reflect.Field;

/**
 * @author songshuiyang
 * @date 2018/10/30 22:27
 */
@Data
public class ColumnEntity {
    /**
     * 实体类属性名
     */
    private String fieldName;
    /**
     * 表列名
     */
    private String columnName;
    /**
     * field
     */
    private Field field;

}
