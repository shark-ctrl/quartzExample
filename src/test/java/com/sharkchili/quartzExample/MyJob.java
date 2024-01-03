package com.sharkchili.quartzExample;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@PersistJobDataAfterExecution
public class MyJob implements Job {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    private Integer count;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ++count;
        jobExecutionContext.getJobDetail().getJobDataMap().put("count", count);
        log.info("任务执行时间:{} ，JobDataMap:{} name:{}", dateTime, JSONUtil.toJsonStr(jobExecutionContext.getJobDetail().getJobDataMap()), name);
    }
}