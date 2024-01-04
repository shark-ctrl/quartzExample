package com.sharkchili.quartzExample;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

@Slf4j
public class MyJobListener implements JobListener {
    @Override
    public String getName() {
        String name = getClass().getSimpleName();
        log.info("监听器的名称是：" + name);
        return name;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        String jobName = context.getJobDetail().getKey().getName();
        log.info("Job的名称是：" + jobName + " Scheduler在JobDetail将要被执行时调用这个方法");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        String jobName = context.getJobDetail().getKey().getName();
        log.info("Job的名称是：" + jobName + " Scheduler在JobDetail即将被执行，但又被TriggerListerner否决时会调用该方法");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        String jobName = context.getJobDetail().getKey().getName();
        log.info("Job的名称是：" + jobName + " Scheduler在JobDetail被执行之后调用这个方法");
    }
}
