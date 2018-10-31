package com.songsy.iframe.core.persistence.provider.utils;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.songsy.iframe.core.persistence.provider.CrudProvider;
import com.songsy.iframe.core.persistence.provider.annotation.*;
import com.songsy.iframe.core.persistence.provider.entity.ColumnEntity;
import com.songsy.iframe.core.persistence.provider.entity.IdColumnEntity;
import com.songsy.iframe.core.persistence.provider.entity.TableEntity;
import com.songsy.iframe.core.persistence.provider.exception.EntityAnnotationException;
import com.songsy.iframe.core.persistence.provider.exception.EntityException;
import com.songsy.iframe.core.persistence.provider.threadlocal.EntityProperty;
import com.songsy.iframe.core.persistence.provider.threadlocal.EntityThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * mybatis
 * @author songshuiyang
 * @date 2018/10/30 22:40
 */
public class MybatisTableUtils {

    private static Logger logger = LoggerFactory.getLogger(CrudProvider.class);

    // 表结构缓存
    private static Map<String, TableEntity> mybatisTableMap = Maps.newHashMap();

    /**
     * 获取当前运行线程对应mapper接口 实体类属性
     *
     * @return
     */
    public static TableEntity getCurrentTableEntity() {
        EntityProperty entityProperty = EntityThreadLocal.get();
        if (entityProperty.getEntityClass().equals(Object.class)) {
            throw new EntityException();
        }
        Class entityClass = entityProperty.getEntityClass();
        return getTableEntity(entityClass);
    }


    /**
     * 根据实体类名获取表结构
     *
     * @param clazz
     * @return
     */
    public static TableEntity getTableEntity(Class clazz) {
        String className = clazz.getName();
        // 如果表结构缓存中没有
        if (!mybatisTableMap.containsKey(className)) {
            TableEntity tableEntity = new TableEntity();
            List<ColumnEntity> columnEntities = new ArrayList<>();
            Entity entity = (Entity) clazz.getAnnotation(Entity.class);
            Table table = (Table) clazz.getAnnotation(Table.class);
            if (null != table && null != entity) {
                // 表名
                if (StringUtils.isNotBlank(table.name())) {
                    tableEntity.setTableName(table.name());
                } else { // 如果@Table注解表名为空则取实体类名
                    tableEntity.setTableName(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, clazz.getSimpleName()));
                }
                // 获取所有的字段
                List<Field> fieldList = ReflectionUtils.getFields(clazz);
                for (Field field : fieldList) {
                    Class fieldClass = field.getType();
                    String fieldName = field.getName();
                    // 忽略字段
                    if ("serialVersionUID".equals(fieldName)
                            || field.getAnnotation(Transient.class) != null
                            || Collection.class.isAssignableFrom(fieldClass)
                            || Map.class.isAssignableFrom(fieldClass)) {
                        continue;
                    }
                    // 如果是主键字段
                    if (null != field.getAnnotation(Id.class)) {
                        IdColumnEntity idColumnEntity = new IdColumnEntity();
                        ColumnEntity columnEntity = getColumn(field);
                        idColumnEntity.setColumnName(columnEntity.getColumnName());
                        idColumnEntity.setFieldName(columnEntity.getFieldName());
                        // 获取主键生成规则
                        GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
                        if (null != generatedValue && !generatedValue.strategy().equals(GenerationType.IDENTITY)) {
                            idColumnEntity.setGenerationType(generatedValue.strategy());
                        } else {
                            // 默认为主键由数据库自动生成（主要是自动增长型）
                            idColumnEntity.setGenerationType(GenerationType.IDENTITY);
                        }
                        tableEntity.setIdColumnEntity(idColumnEntity);
                        // id 属性不保存在List<ColumnEntity> columnEntities 中
                        continue;
                    }
                    columnEntities.add(getColumn(field));
                }
            } else {
                throw new EntityAnnotationException(className);
            }
            tableEntity.setColumnEntities(columnEntities);
            mybatisTableMap.put(clazz.getName(), tableEntity);
        }
        return mybatisTableMap.get(clazz.getName());
    }

    /**
     * 根据Field获取对应的列属性
     *
     * @param field
     * @return
     */
    private static ColumnEntity getColumn(Field field) {
        ColumnEntity columnEntity = new ColumnEntity();
        String fieldName = field.getName();
        Column columnAnnotation = field.getAnnotation(Column.class);
        // 如果实体类属性加了@Column 则取注解里面的字段名
        if (null != columnAnnotation && StringUtils.isNotBlank(columnAnnotation.name())) {
            columnEntity.setColumnName(columnAnnotation.name());
        } else { // 默认为属性的字段名
            columnEntity.setColumnName(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName));
        }
        columnEntity.setFieldName(fieldName);
        return columnEntity;
    }


}
