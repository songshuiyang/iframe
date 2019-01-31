package com.songsy.iframe.core.elasticjob;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobPacket implements Serializable {

    private final Class<? extends ElasticJob> elasticJobClass;
    /**
     * 作业名称，必须
     */
    private final String jobName;

    /**
     * cron表达式，用于配置作业触发时间，必须
     */
    private final String cron;

    /**
     * 作业分片总数，必须
     */
    private final int shardingTotalCount;

    /**
     * 分片序列号和参数用等号分隔，多个键值对用逗号分隔
     * 分片序列号从0开始，不可大于或等于作业分片总数
     * 如：
     * 0=a,1=b,2=c
     */
    private String shardingItemParameters;

    /**
     * 作业自定义参数
     * 作业自定义参数，可通过传递该参数为作业调度的业务方法传参，用于实现带参数的作业
     * 例：每次获取的数据量、作业实例从数据库读取的主键等
     */
    private String jobParameter;

    /**
     * 是否开启失效转移，开启表示如果作业在一次任务执行中途宕机，允许将该次未完成的任务在另一作业节点上补偿执行
     */
    private boolean failover;

    /**
     * 是否开启错过任务重新执行
     */
    private boolean misfire = true;

    /**
     * 作业描述信息
     */
    private String description;

    /**
     * 任务监听
     */
    private List<ElasticJobListener> jobListeners = new ArrayList<>();

    /**
     * Job时间通知
     */
    private JobEventConfiguration jobEventConfiguration;

    /**
     * 作业定制化属性，目前支持job_exception_handler和executor_service_handler，用于扩展异常处理和自定义作业处理线程池
     */
    private Map<String, String> jobProperties = new HashMap<>();

    /**
     * DATAFLOW类型作业，是否流式处理数据
     * 如果流式处理数据, 则fetchData不返回空结果将持续执行作业
     * 如果非流式处理数据, 则处理数据完成后作业结束
     */
    private boolean streamingProcess;

    /**
     * 判断是否是单一作业，若为true，则不会有重复的job创建
     */
    private boolean singleJob = false;

    public JobPacket(Class<? extends ElasticJob> elasticJobClass, String jobName, String cron, int shardingTotalCount) {
        this.elasticJobClass = elasticJobClass;
        this.jobName = jobName;
        this.cron = cron;
        this.shardingTotalCount = shardingTotalCount;
    }

    public JobPacket(Class<? extends ElasticJob> elasticJobClass, String jobName, String cron, int shardingTotalCount, boolean singleJob) {
        this.elasticJobClass = elasticJobClass;
        this.jobName = jobName;
        this.cron = cron;
        this.shardingTotalCount = shardingTotalCount;
        this.singleJob = singleJob;
    }

    public String getShardingItemParameters() {
        return shardingItemParameters;
    }

    public void setShardingItemParameters(String shardingItemParameters) {
        this.shardingItemParameters = shardingItemParameters;
    }

    public String getJobParameter() {
        return jobParameter;
    }

    public void setJobParameter(String jobParameter) {
        this.jobParameter = jobParameter;
    }

    public boolean isFailover() {
        return failover;
    }

    public void setFailover(boolean failover) {
        this.failover = failover;
    }

    public boolean isMisfire() {
        return misfire;
    }

    public void setMisfire(boolean misfire) {
        this.misfire = misfire;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ElasticJobListener> getJobListeners() {
        return jobListeners;
    }

    public void setJobListeners(List<ElasticJobListener> jobListeners) {
        this.jobListeners = jobListeners;
    }

    public JobEventConfiguration getJobEventConfiguration() {
        return jobEventConfiguration;
    }

    public void setJobEventConfiguration(JobEventConfiguration jobEventConfiguration) {
        this.jobEventConfiguration = jobEventConfiguration;
    }

    public Map<String, String> getJobProperties() {
        return jobProperties;
    }

    public Map<String, String> jobProperties(final String key, final String value) {
        jobProperties.put(key, value);
        return jobProperties;
    }

    public boolean isSingleJob() {
        return singleJob;
    }

    public void setSingleJob(boolean singleJob) {
        this.singleJob = singleJob;
    }

    public boolean isStreamingProcess() {
        return streamingProcess;
    }

    public void setStreamingProcess(boolean streamingProcess) {
        this.streamingProcess = streamingProcess;
    }

    public Class<? extends ElasticJob> getElasticJobClass() {
        return elasticJobClass;
    }

    public String getJobName() {
        return jobName;
    }

    public String getCron() {
        return cron;
    }

    public int getShardingTotalCount() {
        return shardingTotalCount;
    }
}
