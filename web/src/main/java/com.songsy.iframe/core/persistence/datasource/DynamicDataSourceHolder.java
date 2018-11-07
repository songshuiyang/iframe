package com.songsy.iframe.core.persistence.datasource;

import org.springframework.util.Assert;

/**
 * 保存当前线程绑定的数据源信息
 * @author songsy
 * @Date 2018/11/7 17:18
 */
public class DynamicDataSourceHolder {

    private static final ThreadLocal<String> dataSourceHolder = new ThreadLocal<>();

    public static void setDataSource(String dataSource) {
        Assert.notNull(dataSource, "dataSource cannot be null");
        dataSourceHolder.set(dataSource);
    }

    public static String getDataSource() {
        return dataSourceHolder.get();
    }

    public static void removeDataSource() {
        dataSourceHolder.remove();
    }

}
