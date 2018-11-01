package com.songsy.iframe.core.persistence.provider.entity;

import lombok.Data;

import java.util.List;

/**
 * 表属性
 * @author songshuiyang
 * @date 2018/10/30 22:21
 */
@Data
public class TableEntity {
    /**
     * 表名
     */
    String tableName;
    /**
     * 主键列
     */
    IdColumnEntity idColumnEntity;
    /**
     * 逻辑删除标识列
     */
    ColumnEntity deleteColunmEntity;
    /**
     * 列属性(除了id)
     */
    List<ColumnEntity> columnEntities;

}
