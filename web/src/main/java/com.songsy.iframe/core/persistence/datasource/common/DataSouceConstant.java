package com.songsy.iframe.core.persistence.datasource.common;

/**
 * @author songsy
 * @Date 2018/11/7 18:35
 */
public interface DataSouceConstant {
    // 数据源默认初始链接数
    int DEFAULT_DATASOURCE_INIT_SIZE = 1;
    // 数据源默认最大连接数
    int DEFAULT_DATASOURCE_MAX_ACTIVE = 100;
    // 数据源默认最小连接数
    int DEFAULT_DATASOURCE_MIN_IDLE = 1;
    // 数据源默认配置获取连接等待超时的时间
    int DEFAULT_DATASOURCE_MAX_WAIT = 6000;
    // 主数据库前缀
    String MASTER_DATA_SOURCE_PREFIX = "master";
    // 从数据库前缀
    String SLAVE_DATA_SOURCE_PREFIX = "slave";

    String[] DATA_SOURCE_PREFIX = {"master", "slave"};
}
