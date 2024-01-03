package com.sharkchili.quartzExample;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class MyJob implements Job {

    private String name;

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("任务执行时间:{} ，JobDataMap:{} name:{}", dateTime, JSONUtil.toJsonStr(jobExecutionContext.getJobDetail().getJobDataMap()), name);
    }
}