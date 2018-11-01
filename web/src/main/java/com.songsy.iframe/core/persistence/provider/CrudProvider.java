package com.songsy.iframe.core.persistence.provider;

import com.google.common.collect.Lists;
import com.songsy.iframe.core.persistence.provider.entity.ColumnEntity;
import com.songsy.iframe.core.persistence.provider.entity.TableEntity;
import com.songsy.iframe.core.persistence.provider.utils.MybatisTableUtils;
import com.songsy.iframe.core.persistence.provider.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 通用增删改查实现方法
 * @author songshuiyang
 * @date 2018/10/28 11:34
 */
public class CrudProvider {

    private static Logger logger = LoggerFactory.getLogger(CrudProvider.class);

    public static final String FIND_ALL = "findAll";
    public static final String FIND_BY_ID = "findById";
    public static final String INSERT = "insert";
    public static final String UPDATE = "update";

    /**
     * 查询所有数据
     * @return
     */
    public String findAll() {
        TableEntity tableEntity = MybatisTableUtils.getCurrentTableEntity();
        String sql = "SELECT * FROM " +  tableEntity.getTableName();
        return sql;
    }

    /**
     * 根据id查询记录
     * @param id
     * @return
     */
    public String findById (Object id) {
        TableEntity tableEntity = MybatisTableUtils.getCurrentTableEntity();
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(" * ");
        sb.append("FROM");
        sb.append(" ").append(tableEntity.getTableName()).append(" ");
        sb.append(" WHERE ").append(tableEntity.getIdColumnEntity().getColumnName()).append("=").append(id);
        return sb.toString();
    }

    /**
     * 插入记录
     * @param entity
     */
    public String insert (Object entity) {
        TableEntity tableEntity = MybatisTableUtils.getCurrentTableEntity();
        List<ColumnEntity> columnEntities = tableEntity.getColumnEntities();
        List<String> fieldNames = Lists.newArrayList();
        List<String> columnNames = Lists.newArrayList();
        for (ColumnEntity columnEntity : columnEntities) {
            Object value = ReflectionUtils.getFieldValue(entity, columnEntity.getFieldName());
            // 字段为null不插入
            if (value != null) {
                columnNames.add(columnEntity.getColumnName());
                fieldNames.add("#{" + columnEntity.getFieldName() + "}");
            }
        }
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(tableEntity.getTableName());
        sb.append(" (");
        sb.append(StringUtils.join(columnNames, ","));
        sb.append(") ");
        sb.append(" VALUES(");
        sb.append(StringUtils.join(fieldNames, ","));
        sb.append(")");
        String sql = sb.toString();
        return sql;
    }

    /**
     * 更新记录
     * @param entity
     */
    public String  update (Object entity) {
        return "";
    }
}
