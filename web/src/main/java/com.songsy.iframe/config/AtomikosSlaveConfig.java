package com.songsy.iframe.config;

import com.songsy.iframe.core.persistence.datasource.common.DataSouceConstant;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 从库配置
 *
 * @author songsy
 * @date 2019/10/23 18:53
 */
@Configuration
@MapperScan(value = "com.songsy.iframe.mapper.slave", sqlSessionFactoryRef = "slaveSqlSessionFactory")
public class AtomikosSlaveConfig extends AbstractAtomikosConfig {

    /**
     * 从库数据源
     *
     * @return
     */
    @Bean(name = "slaveDataSource")
    public DataSource masterDataSource() {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        // DataSouceConstant.SLAVE_DATA_SOURCE_PREFIX = slave
        atomikosDataSourceBean.setXaDataSource(createXaDataSource(DataSouceConstant.SLAVE_DATA_SOURCE_PREFIX));
        atomikosDataSourceBean.setUniqueResourceName(DataSouceConstant.SLAVE_DATA_SOURCE_PREFIX);
        atomikosDataSourceBean.setPoolSize(5);
        return atomikosDataSourceBean;
    }

    /**
     * 从库数据源 绑定的SessionFactory
     *
     * @return
     */
    @Bean(name = "slaveSqlSessionFactory")
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("slaveDataSource") DataSource masterDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        return sessionFactory.getObject();
    }

}
