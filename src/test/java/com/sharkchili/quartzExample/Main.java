package com.sharkchili.quartzExample;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        // 获取任务调度的实例
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        for (int i = 0; i < 10; i++) {
            int taskId = RandomUtil.randomInt(100);

            // 定义任务调度实例, 并与TestJob绑定
            JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                    .withIdentity("myJob-" + taskId, "myJobGroup")
                    .build();
            //获取封装任务重要属性的类
            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            String name = jobDetail.getKey().getName();
            String group = jobDetail.getKey().getGroup();
            String jobClassName = jobDetail.getJobClass().getName();
            log.info("jobDataMap:{},name:{},group:{},jobClassName:{}", JSONUtil.toJsonStr(jobDataMap), name, group, jobClassName);

            jobDataMap.put("id", taskId);

            // 定义触发器, 会马上执行一次, 接着60秒执行一次
            Trigger trigger = TriggerBuilder.newTrigger()
                    //在name重复的情况下会报错
                    .withIdentity("testTrigger-" + taskId, "testTriggerGroup")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(10))
                    .build();

            // 使用触发器调度任务的执行
            scheduler.scheduleJob(jobDetail, trigger);
        }


        // 开启任务
        scheduler.start();
    }
}
