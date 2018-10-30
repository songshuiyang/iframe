package com.songsy.iframe.core.persistence.provider.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.songsy.iframe.core.persistence.provider.CrudProvider;
import com.songsy.iframe.core.persistence.provider.annotation.Entity;
import com.songsy.iframe.core.persistence.provider.annotation.Table;
import com.songsy.iframe.core.persistence.provider.entity.TableEntity;
import com.songsy.iframe.core.persistence.provider.exception.EntityAnnotationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author songshuiyang
 * @date 2018/10/30 22:40
 */
public class MybatisTableUtils {

    private static Logger logger = LoggerFactory.getLogger(CrudProvider.class);

    // 表结构缓存
    private static Map<String,TableEntity> mybatisTableMap = Maps.newHashMap();

    /**
     * 根据实体类名获取表结构
     *
     * @param clazz
     * @return
     */
    public static TableEntity getTableEntity(Class clazz) {
        // 如果表结构缓存中没有
        if(!mybatisTableMap.containsKey(clazz.getName())) {
            TableEntity tableEntity = null;
            Entity entity = (Entity) clazz.getAnnotation(Entity.class);
            Table table = (Table) clazz.getAnnotation(Table.class);
            if (null != table && null != entity) {


            } else {
                throw new EntityAnnotationException();
            }
            mybatisTableMap.put(clazz.getName(), tableEntity);

        }
        return mybatisTableMap.get(clazz.getName());
    }

    /**
     * 根据类名获取其所有的属性值
     * @param clazz
     * @return
     */
    private static List<Field> getFields(Class clazz) {
        List<Field> fields= Lists.newArrayList();
        Class current = clazz;
        while (!current.getName().equals(Object.class.getName())) {
            getFields(fields,current);
            current = current.getSuperclass();
        }
        return fields;
    }

    private static void getFields(List<Field> fields,Class clazz) {
        for(Field field:clazz.getDeclaredFields()) {
            fields.add(field);
        }
    }
}
