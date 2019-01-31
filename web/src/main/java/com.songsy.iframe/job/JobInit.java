package com.songsy.iframe.job;

import com.songsy.iframe.core.elasticjob.ElasticJobService;
import com.songsy.iframe.core.elasticjob.JobPacket;
import com.songsy.iframe.job.constant.JobConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author songsy
 * @date 2019/1/31 16:04
 */
@Slf4j
@Component
public class JobInit {

    @Autowired
    private ElasticJobService elasticJobService;

    @EventListener(ApplicationReadyEvent.class)
    private void init() {
        registerSingleJob1();
        registerSingleJob2();

    }
    private void registerSingleJob1 () {
        String jobName = JobConstant.FIRST_JOB_NAME;
        JobPacket jobPacket = new JobPacket(FirstJob.class, jobName, JobConstant.FIRST_JOB_CRON, 1);
        elasticJobService.registerSingleJob(jobPacket);

    }

    private void registerSingleJob2 () {
        String jobName = JobConstant.SECOND_JOB_NAME;
        JobPacket jobPacket = new JobPacket(SecondJob.class, jobName, JobConstant.SECOND_JOB_CRON, 1);
        elasticJobService.registerSingleJob(jobPacket);
    }
}
