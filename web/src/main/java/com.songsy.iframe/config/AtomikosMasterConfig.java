package com.songsy.iframe.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.songsy.iframe.core.persistence.datasource.common.DataSouceConstant;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;

/**
 * 主库配置
 *
 * @author songsy
 * @date 2019/10/23 18:53
 */
@Configuration
@EnableTransactionManagement
@MapperScan(value = "com.songsy.iframe.mapper.master", sqlSessionFactoryRef = "masterSqlSessionFactory")
public class AtomikosMasterConfig extends AbstractAtomikosConfig {

    /**
     * 使用这个来做总事务 后面的数据源就不用设置事务了
     *
     * @return
     */
    @Bean(name = "transactionManager")
    @Primary
    public JtaTransactionManager regTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        UserTransaction userTransaction = new UserTransactionImp();
        return new JtaTransactionManager(userTransaction, userTransactionManager);
    }

    /**
     * 主库数据源
     *
     * @return
     */
    @Bean(name = "masterDataSource")
    @Primary
    public DataSource masterDataSource() {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        // DataSouceConstant.MASTER_DATA_SOURCE_PREFIX = master
        atomikosDataSourceBean.setXaDataSource(createXaDataSource(DataSouceConstant.MASTER_DATA_SOURCE_PREFIX));
        atomikosDataSourceBean.setUniqueResourceName(DataSouceConstant.MASTER_DATA_SOURCE_PREFIX);
        atomikosDataSourceBean.setPoolSize(5);
        return atomikosDataSourceBean;
    }

    /**
     * 主库数据源 绑定的SessionFactory
     *
     * @return
     */
    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDataSource") DataSource masterDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        return sessionFactory.getObject();
    }

}
