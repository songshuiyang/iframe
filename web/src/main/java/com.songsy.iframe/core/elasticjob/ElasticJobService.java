package com.songsy.iframe.core.elasticjob;


import java.util.List;

public interface ElasticJobService {

    /**
     * 注册一个Job
     *
     * @param job
     */
    void registerJob(JobPacket job);

    /**
     * 注册单个Job，若Job已经存在，则不会添加
     *
     * @param job
     */
    void registerSingleJob(JobPacket job);

    /**
     * 获取Job实例数
     *
     * @param jobName
     * @return
     */
    List<String> getJobInstance(String jobName);

    /**
     * 作业关闭
     *
     * @param jobName
     */
    void shutdown(String jobName);

    /**
     * 作业删除
     *
     * @param jobName
     */
    void remove(String jobName);

    /**
     * 作业修改
     */
    void update(JobPacket jobPacket);
}
