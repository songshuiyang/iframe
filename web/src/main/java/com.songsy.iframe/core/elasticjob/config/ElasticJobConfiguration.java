package com.songsy.iframe.core.elasticjob.config;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;


@Configuration
@EnableConfigurationProperties({ZookeeperConfig.class})
public class ElasticJobConfiguration {

    @Autowired
    private ZookeeperConfig zookeeperConfig;

    /**
     * Zookeeper连接配置
     *
     * @return
     */
    @Bean("zookeeperConfiguration")
    @ConditionalOnMissingBean
    public ZookeeperConfiguration zookeeperConfiguration() {
        if (null == zookeeperConfig) {
            zookeeperConfig = new ZookeeperConfig();
        }
        ZookeeperConfiguration configuration = new ZookeeperConfiguration(zookeeperConfig.getServerList(), zookeeperConfig.getNamespace());
        configuration.setBaseSleepTimeMilliseconds(zookeeperConfig.getBaseSleepTimeMilliseconds());
        configuration.setMaxSleepTimeMilliseconds(zookeeperConfig.getMaxSleepTimeMilliseconds());
        configuration.setMaxRetries(zookeeperConfig.getMaxRetries());
        configuration.setSessionTimeoutMilliseconds(zookeeperConfig.getSessionTimeoutMilliseconds());
        configuration.setConnectionTimeoutMilliseconds(zookeeperConfig.getConnectionTimeoutMilliseconds());
        configuration.setDigest(zookeeperConfig.getDigest());
        return configuration;
    }

    /**
     * 注册中心配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    @DependsOn("zookeeperConfiguration")
    public CoordinatorRegistryCenter registryCenter() {
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(zookeeperConfiguration());
        regCenter.init();
        return regCenter;
    }

}
