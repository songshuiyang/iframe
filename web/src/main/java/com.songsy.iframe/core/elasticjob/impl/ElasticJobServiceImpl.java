package com.songsy.iframe.core.elasticjob.impl;


import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.internal.config.LiteJobConfigurationGsonFactory;
import com.dangdang.ddframe.job.lite.internal.storage.JobNodePath;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.songsy.iframe.core.elasticjob.ElasticJobService;
import com.songsy.iframe.core.elasticjob.JobPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
// TODO 如要使用需去掉注释
//@Service
public class ElasticJobServiceImpl implements ElasticJobService {

    private static Logger log = LoggerFactory.getLogger(ElasticJobServiceImpl.class);

    @Autowired
    private CoordinatorRegistryCenter registryCenter;

    /**
     * 注册一个Job
     *
     * @param jobPacket
     */
    @Override
    public void registerJob(JobPacket jobPacket) {
        log.info("{}定时任务注册开始发布", jobPacket.getJobName());
        List<String> jobInstance = getJobInstance(jobPacket.getJobName());
        if (!jobPacket.isSingleJob() || jobInstance.isEmpty()) {
            log.info("Register a elasticjob");
            JobEventConfiguration jobEventConfiguration = jobPacket.getJobEventConfiguration();
            List<ElasticJobListener> jobListeners = jobPacket.getJobListeners();
            ElasticJobListener[] elasticJobListeners = new ElasticJobListener[jobListeners.size()];
            jobPacket.getJobListeners().toArray(elasticJobListeners);
            JobScheduler jobScheduler;
            if (jobEventConfiguration != null) {
                jobScheduler = new JobScheduler(registryCenter, createJobConfiguration(jobPacket), jobEventConfiguration, elasticJobListeners);
            } else {
                jobScheduler = new JobScheduler(registryCenter, createJobConfiguration(jobPacket), elasticJobListeners);
            }
            jobScheduler.init();
            log.info("{}定时任务注册发布成功", jobPacket.getJobName());
        } else {
            log.info("{}定时任务已经注册（无法再次注册）", jobPacket.getJobName());
        }
    }

    /**
     * 注册单个Job，若Job已经存在，则不会添加
     *
     * @param jobPacket
     */
    @Override
    public void registerSingleJob(JobPacket jobPacket) {
        log.info("Register a single elasticjob");
        jobPacket.setSingleJob(true);
        registerJob(jobPacket);
    }

    @Override
    public List<String> getJobInstance(String jobName) {
        Assert.notNull(jobName, "JobName can not be empty");
        JobNodePath jobNodePath = new JobNodePath(jobName);
        String instancesNodePath = jobNodePath.getInstancesNodePath();
        return registryCenter.getChildrenKeys(instancesNodePath);
    }

    @Override
    public void shutdown(String jobName) {
        Assert.notNull(jobName, "JobName can not be empty");
        JobNodePath jobNodePath = new JobNodePath(jobName);
        for (String each : registryCenter.getChildrenKeys(jobNodePath.getInstancesNodePath())) {
            registryCenter.remove(jobNodePath.getInstanceNodePath(each));
        }
    }

    @Override
    public void remove(String jobName) {
        shutdown(jobName);
        JobNodePath jobNodePath = new JobNodePath(jobName);
        List<String> servers = registryCenter.getChildrenKeys(jobNodePath.getServerNodePath());
        for (String each : servers) {
            registryCenter.remove(jobNodePath.getServerNodePath(each));
        }
        registryCenter.remove("/" + jobName);
    }


    @Override
    public void update(JobPacket jobPacket) {
        JobNodePath jobNodePath = new JobNodePath(jobPacket.getJobName());
        registryCenter.update(jobNodePath.getConfigNodePath(), LiteJobConfigurationGsonFactory.toJsonForObject(jobPacket));
    }


    /**
     * 创建任务配置
     *
     * @param jobPacket
     * @return
     */
    private static LiteJobConfiguration createJobConfiguration(JobPacket jobPacket) {
        // 定义作业核心配置
        JobCoreConfiguration.Builder builder = JobCoreConfiguration.newBuilder(jobPacket.getJobName(), jobPacket.getCron(), jobPacket.getShardingTotalCount());
        //处理其他非必要的属性
        builder.shardingItemParameters(jobPacket.getShardingItemParameters());
        builder.jobParameter(jobPacket.getJobParameter());
        builder.failover(jobPacket.isFailover());
        builder.misfire(jobPacket.isMisfire());
        builder.description(jobPacket.getDescription());
        jobPacket.getJobProperties().forEach(builder::jobProperties);

        JobCoreConfiguration simpleCoreConfig = builder.build();
        JobTypeConfiguration jobTypeConfiguration = null;
        Class elasticJobClass = jobPacket.getElasticJobClass();
        try {
            Object instance = elasticJobClass.newInstance();
            if (instance instanceof SimpleJob) { // 定义Simple类型配置
                jobTypeConfiguration = new SimpleJobConfiguration(simpleCoreConfig, elasticJobClass.getCanonicalName());
            } else {// 定义Dataflow类型配置
                jobTypeConfiguration = new DataflowJobConfiguration(simpleCoreConfig, elasticJobClass.getCanonicalName(), jobPacket.isStreamingProcess());
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        // 定义Lite作业根配置 设置本地配置是否可覆盖注册中心配置
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(jobTypeConfiguration).overwrite(true).build();
        return simpleJobRootConfig;
    }
}
