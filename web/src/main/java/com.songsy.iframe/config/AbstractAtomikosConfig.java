package com.songsy.iframe.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.songsy.iframe.core.persistence.datasource.common.DataSouceConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author songsy
 * @date 2019/10/24 10:31
 */
@Slf4j
public abstract class AbstractAtomikosConfig {

    @Autowired
    private Environment env;

    /**
     * 创建数据源
     *
     * @param prefix
     * @return
     */
    protected DruidXADataSource createXaDataSource(String prefix) {
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
                log.debug("get datasource from jndi - [{}].", jndiName);
                Context context = new InitialContext();
                DruidXADataSource dataSource = (DruidXADataSource) context.lookup(jndiName);
                return dataSource;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else {
            log.debug("create druid datasource.");
            log.debug("url - {}.", url);
            log.debug("username - {}.", username);
            log.debug("password - {}.", password);
            log.debug("driverClass - {}.", driverClass);
            log.debug("initialSize - {}.", initialSize);
            log.debug("maxActive - {}.", maxActive);
            log.debug("minIdle - {}.", minIdle);
            try {
                DruidXADataSource datasource = new DruidXADataSource();
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
