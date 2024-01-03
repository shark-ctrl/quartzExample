package com.sharkchili.quartzExample;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class Main {
    public static void main(String[] args) throws Exception {
        // 获取任务调度的实例
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 定义任务调度实例, 并与TestJob绑定
        JobDetail job = JobBuilder.newJob(MyJob.class)
                .withIdentity("myJob", "myJobGroup")
                .build();

        // 定义触发器, 会马上执行一次, 接着1秒执行一次
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("testTrigger", "testTriggerGroup")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1))
                .build();

        // 使用触发器调度任务的执行
        scheduler.scheduleJob(job, trigger);

        // 开启任务
        scheduler.start();
    }
}
