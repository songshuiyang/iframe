package com.songsy.iframe.core.persistence.provider.entity;

import java.util.List;

/**
 * 表属性
 * @author songshuiyang
 * @date 2018/10/30 22:21
 */
public class TableEntity {
    /**
     * 表名
     */
    String tableName;
    /**
     * 主键
     */
    ColumnEntity id;
    /**
     * 主键生成类型
     */
    String idGenerationType;
    /**
     * 列属性
     */
    List<ColumnEntity> columnEntities;

}
