package com.songsy.iframe.core.persistence.provider;

import com.google.common.collect.Lists;
import com.songsy.iframe.core.persistence.provider.annotation.Version;
import com.songsy.iframe.core.persistence.provider.entity.ColumnEntity;
import com.songsy.iframe.core.persistence.provider.entity.TableEntity;
import com.songsy.iframe.core.persistence.provider.utils.MybatisTableUtils;
import com.songsy.iframe.core.persistence.provider.utils.PageUtils;
import com.songsy.iframe.core.persistence.provider.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 通用增删改查实现方法
 *
 * @author songshuiyang
 * @date 2018/10/28 11:34
 */
public class CrudProvider {

    private static Logger logger = LoggerFactory.getLogger(CrudProvider.class);

    public static final String FIND_ALL = "findAll";
    public static final String FIND_BY_ID = "findById";
    public static final String INSERT = "insert";
    public static final String UPDATE = "update";
    public static final String UPDATE_NULL = "updateNull";
    public static final String DELETE_ONE = "deleteOne";
    public static final String LOGIC_DELETE_ONE = "logicDeleteOne";
    public static final String FIND_AUTO_BY_PAGE = "findAutoByPage";

    /**
     * 查询所有数据
     *
     * @return
     */
    public String findAll() {
        TableEntity tableEntity = MybatisTableUtils.getCurrentTableEntity();
        String sql = "SELECT * FROM " + tableEntity.getTableName();
        return sql;
    }

    /**
     * 根据id查询记录
     *
     * @param id
     * @return
     */
    public String findById(Object id) {
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
     *
     * @param entity
     */
    public String insert(Object entity) {
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
     * 字段属性为null不更新
     *
     * @param entity
     */
    public String update(Object entity) {
        TableEntity tableEntity = MybatisTableUtils.getCurrentTableEntity();
        List<ColumnEntity> columnEntities = tableEntity.getColumnEntities();
        ColumnEntity versionColumnEntity = null;
        List<String> updateColumns = Lists.newArrayList();
        for (ColumnEntity columnEntity : columnEntities) {
            // 乐观锁处理 更新后version字段加一
            Field field = columnEntity.getField();
            Version version = field.getAnnotation(Version.class);
            {
                if (version != null) {
                    versionColumnEntity = columnEntity;
                    updateColumns.add(columnEntity.getColumnName() + " = " + columnEntity.getFieldName() + " + 1");
                    continue;
                }
            }
            Object value = ReflectionUtils.getFieldValue(entity, columnEntity.getFieldName());
            if (value != null) {
                updateColumns.add(columnEntity.getColumnName() + " = " + "#{" + columnEntity.getFieldName() + "}");
            }
        }
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(tableEntity.getTableName());
        sb.append(" SET ");
        sb.append(StringUtils.join(updateColumns, ","));
        sb.append(" WHERE ");
        sb.append(tableEntity.getIdColumnEntity().getColumnName());
        sb.append(" = ");
        sb.append("#{" + tableEntity.getIdColumnEntity().getFieldName() + "}");
        sb.append(" and ");
        sb.append(versionColumnEntity.getColumnName());
        sb.append(" = ");
        sb.append("#{" + versionColumnEntity.getFieldName() + "}");
        String sql = sb.toString();
        return sql;
    }

    /**
     * 更新记录
     * 字段属性为null 也会更新为null
     *
     * @param entity
     */
    public String updateNull(Object entity) {
        TableEntity tableEntity = MybatisTableUtils.getCurrentTableEntity();
        List<ColumnEntity> columnEntities = tableEntity.getColumnEntities();
        ColumnEntity versionColumnEntity = null;
        List<String> updateColumns = Lists.newArrayList();
        for (ColumnEntity columnEntity : columnEntities) {
            // 乐观锁处理 更新后version字段加一
            Field field = columnEntity.getField();
            Version version = field.getAnnotation(Version.class);
            {
                if (version != null) {
                    versionColumnEntity = columnEntity;
                    updateColumns.add(columnEntity.getColumnName() + " = " + columnEntity.getFieldName() + " + 1");
                    continue;
                }
            }
            updateColumns.add(columnEntity.getColumnName() + " = " + "#{" + columnEntity.getFieldName() + "}");
        }
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(tableEntity.getTableName());
        sb.append(" SET ");
        sb.append(StringUtils.join(updateColumns, ","));
        sb.append(" WHERE ");
        sb.append(tableEntity.getIdColumnEntity().getColumnName());
        sb.append(" = ");
        sb.append("#{" + tableEntity.getIdColumnEntity().getFieldName() + "}");
        sb.append(" and ");
        sb.append(versionColumnEntity.getColumnName());
        sb.append(" = ");
        sb.append("#{" + versionColumnEntity.getFieldName() + "}");
        String sql = sb.toString();
        return sql;
    }

    /**
     * 根据id物理删除记录
     *
     * @param id
     * @return
     */
    public String deleteOne(Object id) {
        TableEntity tableEntity = MybatisTableUtils.getCurrentTableEntity();
        String sql = "DELETE FROM " + tableEntity.getTableName() + " WHERE " + tableEntity.getIdColumnEntity().getColumnName()
                + " = #{id}";
        return sql;
    }

    /**
     * 根据id逻辑删除记录
     *
     * @param id
     * @return
     */
    public String logicDeleteOne(Object id) {
        TableEntity tableEntity = MybatisTableUtils.getCurrentTableEntity();
        String sql = "UPDATE " +
                tableEntity.getTableName() +
                " SET " + tableEntity.getDeleteColunmEntity().getColumnName() + " = 0 " +
                "WHERE " + tableEntity.getIdColumnEntity().getColumnName() + " = #{id}";
        return sql;
    }

    /**
     * 通用分页查询方法
     * @param page
     * @return
     * @throws ParseException
     */
    public String findAutoByPage(Page<?> page) throws ParseException {
        TableEntity tableEntity = MybatisTableUtils.getCurrentTableEntity();
        Map<String, Object> params = page.getParams();
        StringBuilder sb = new StringBuilder();
        Integer tableKey = 0;
        String asTable = tableEntity.getTableName() + tableKey++;

        sb.append("SELECT ");
        sb.append(PageUtils.getColumns(page, asTable));
        sb.append(" FROM ");
        sb.append(tableEntity.getTableName());
        sb.append(" ");
        sb.append(asTable);
        // 拼装查询条件
        if (!params.isEmpty()) {
            String temp = PageUtils.getWhere(page.getParams(), asTable);
            sb.append(" WHERE ");
            sb.append(temp);
        }
        return sb.toString();
    }

}
