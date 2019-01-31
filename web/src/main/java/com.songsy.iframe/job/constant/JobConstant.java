package com.songsy.iframe.job.constant;

/**
 * @author songsy
 * @date 2019/1/31 16:06
 */
public interface JobConstant {

    String FIRST_JOB_NAME = "iframe_first_job";

    String FIRST_JOB_CRON = "0/2 * * * * ? *";


    String SECOND_JOB_NAME = "iframe_second_job";

    String SECOND_JOB_CRON = "0/10 * * * * ? *";
}
