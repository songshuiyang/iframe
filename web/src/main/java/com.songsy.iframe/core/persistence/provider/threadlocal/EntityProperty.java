package com.songsy.iframe.core.persistence.provider.threadlocal;

import lombok.Data;

/**
 * 实体类属性
 * @author songshuiyang
 * @date 2018/10/30 21:28
 */
@Data
public class EntityProperty {
    /**
     * 实体类类型
     */
    private Class entityClass;
    /**
     * id 类型 用于兼容字符主键及数字主键
     */
    private Class idClass;

    public EntityProperty () {

    }

    public EntityProperty (Class entityClass, Class idClass) {
        this.entityClass = entityClass;
        this.idClass = idClass;
    }
}
