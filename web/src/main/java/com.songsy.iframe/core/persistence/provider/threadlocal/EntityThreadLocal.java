package com.songsy.iframe.core.persistence.provider.threadlocal;

/**
 * ThreadLocal为变量在每个线程中都创建了一个副本，那么每个线程可以访问自己内部的副本变量。
 * 每次调用mapper接口方法的时候，先把实体类的信息存放在ThreadLocal中
 * @author songshuiyang
 * @date 2018/10/30 21:27
 */
public class EntityThreadLocal {

    private static ThreadLocal<EntityProperty> threadLocal = new ThreadLocal<>();

    /**
     * 获取当前线程的实体类属性
     * @return
     */
    public static EntityProperty get () {
        if (null == threadLocal) {
            initialValue();
        }
        return threadLocal.get();
    }

    /**
     * 设置当前线程的实体类属性
     * @param entityProperty
     */
    public static void set(EntityProperty entityProperty) {
        if (entityProperty != null) {
            threadLocal.set(entityProperty);
        }
    }

    /**
     * 清除 threadLocal
     */
    public static void remove() {
        threadLocal.remove();
    }

    /**
     * 默认初始化Object.class
     */
    private static void initialValue() {
        EntityProperty entityProperty = new EntityProperty();
        entityProperty.setEntityClass(Object.class);
        entityProperty.setIdClass(null);
        threadLocal.set(entityProperty);
    }

}
