package com.songsy.iframe.core.persistence.provider.entity;

import com.songsy.iframe.core.persistence.provider.annotation.GenerationType;
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
     * 主键
     */
    IdColumnEntity idColumnEntity;
    /**
     * 列属性(除了id)
     */
    List<ColumnEntity> columnEntities;

}
