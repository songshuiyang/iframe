package com.songsy.iframe.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.songsy.iframe.core.persistence.datasource.DynamicDataSource;
import com.songsy.iframe.core.persistence.datasource.common.DataSouceConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.songsy.iframe.core.persistence.datasource.common.DataSouceConstant.DATA_SOURCE_PREFIX;
import static com.songsy.iframe.core.persistence.datasource.common.DataSouceConstant.MASTER_DATA_SOURCE_PREFIX;

/**
 * @author songsy
 * @Date 2018/11/7 17:09
 */
@Configuration
@EnableTransactionManagement
public class Config implements TransactionManagementConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    @Autowired
    Environment env;

    /**
     * 实现接口 TransactionManagementConfigurer 方法，其返回值代表在拥有多个事务管理器的情况下默认使用的事务管理器
     * @return
     */
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return transactionManager();
    }

    @Bean(name = "dataSource")
    public DynamicDataSource dataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        for (String prefix : DATA_SOURCE_PREFIX) {
            targetDataSources.put(prefix, createDataSource(prefix));
        }
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(targetDataSources.get(MASTER_DATA_SOURCE_PREFIX));
        return dynamicDataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager txManager = new DataSourceTransactionManager();
        txManager.setDataSource(dataSource());
        return txManager;
    }

    private DataSource createDataSource(String prefix) {
        // 是否使用数据源
        boolean useJndi = env.getProperty(prefix + "." + "datasource.use-jndi", Boolean.class, false);
        // 数据源名称
        String jndiName = env.getProperty(prefix + "." + "datasource.jndi-name", "");
        // 数据库链接
        String url = env.getProperty(prefix + "." + "datasource.url", "");
        String username = env.getProperty(prefix + "." + "datasource.username", "");
        String password = env.getProperty(prefix + "." + "datasource.password", "");
        String driverClass = env.getProperty(prefix + "." + "datasource.driver-class", "");
        // 数据源默认初始链接数
        int initialSize = env.getProperty(prefix + "." + "datasource.initial-size", Integer.class,
                DataSouceConstant.DEFAULT_DATASOURCE_INIT_SIZE);
        // 数据源最大连接数
        int maxActive = env.getProperty(prefix + "." + "datasource.max-active", Integer.class,
                DataSouceConstant.DEFAULT_DATASOURCE_MAX_ACTIVE);
        // 数据源最小连接数
        int minIdle = env.getProperty(prefix + "." + "datasource.min-idle", Integer.class,
                DataSouceConstant.DEFAULT_DATASOURCE_MIN_IDLE);
        // 配置获取连接等待超时的时间
        int maxWait = env.getProperty(prefix + "." + "datasource.max-wait", Integer.class,
                DataSouceConstant.DEFAULT_DATASOURCE_MAX_WAIT);
        if (useJndi) {
            try {
                logger.debug("get datasource from jndi - [{}].", jndiName);
                Context context = new InitialContext();
                DataSource dataSource = (DataSource) context.lookup(jndiName);
                return dataSource;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            logger.debug("create druid datasource.");
            logger.debug("url - {}.", url);
            logger.debug("username - {}.", username);
            logger.debug("password - {}.", password);
            logger.debug("driverClass - {}.", driverClass);
            logger.debug("initialSize - {}.", initialSize);
            logger.debug("maxActive - {}.", maxActive);
            logger.debug("minIdle - {}.", minIdle);

            try {
                DruidDataSource datasource = new DruidDataSource();
                datasource.setUrl(url);
                datasource.setDriverClassName(driverClass);
                datasource.setUsername(username);
                datasource.setPassword(password);
                datasource.setInitialSize(initialSize);
                datasource.setMaxActive(maxActive);
                datasource.setMinIdle(minIdle);
                datasource.setMaxWait(maxWait);
                datasource.setFilters("stat,slf4j");
                datasource.setProxyFilters(getDruidFilters());
                return datasource;
            } catch (Exception e) {
            }

        }
        return null;
    }

    public List<Filter> getDruidFilters() {
        Slf4jLogFilter slf4jLogFilter = new Slf4jLogFilter();
        slf4jLogFilter.setDataSourceLogEnabled(false);
        slf4jLogFilter.setStatementLogEnabled(false);
        slf4jLogFilter.setStatementExecutableSqlLogEnable(true);
        slf4jLogFilter.setResultSetLogEnabled(false);
        slf4jLogFilter.setResultSetCloseAfterLogEnabled(false);
        slf4jLogFilter.setConnectionLogEnabled(false);

        List<Filter> filters = new ArrayList<>();
        filters.add(new StatFilter());
        filters.add(slf4jLogFilter);
        return filters;
    }
}
