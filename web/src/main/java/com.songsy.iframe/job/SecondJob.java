package com.songsy.iframe.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;

/**
 * @author songsy
 * @date 2019/1/31 16:03
 */
@Slf4j
@DisallowConcurrentExecution
public class SecondJob implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("SecondJob 定时任务----------------");
    }
}
