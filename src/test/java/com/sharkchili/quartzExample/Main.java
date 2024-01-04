package com.sharkchili.quartzExample;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        setTasksWithinTime();
    }

    public static void baseExample() throws SchedulerException {
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

    public void multitaskExample() throws SchedulerException {
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

    /**
     * job和trigger设置同一个key的情况下，反射后的值会以后者为主
     */
    @SneakyThrows
    public static void errorExamlme() {
        // 获取任务调度的实例
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 定义任务调度实例, 并与TestJob绑定
        JobDetail job = JobBuilder.newJob(MyJob.class)
                .usingJobData("name", "JobDetail")
                .usingJobData("count", 1)
                .withIdentity("myJob", "myJobGroup")
                .build();

        // 定义触发器, 会马上执行一次, 接着5秒执行一次
        Trigger trigger = TriggerBuilder.newTrigger()
                .usingJobData("name", "trigger")
                .withIdentity("testTrigger", "testTriggerGroup")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(5))
                .build();

        // 使用触发器调度任务的执行
        scheduler.scheduleJob(job, trigger);

        // 开启任务
        scheduler.start();
    }


    /**
     * 设置时间限制以内的任务
     *
     * @throws SchedulerException
     */
    public static void setTasksWithinTime() throws SchedulerException {
        // 获取任务调度的实例
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 定义任务调度实例, 并与TestJob绑定
        JobDetail job = JobBuilder.newJob(MyJob.class)
                .withIdentity("myJob", "myJobGroup")
                .usingJobData("count", 1)
                .build();

        DateTime startTime = DateUtil.date();
        DateTime endTime = DateUtil.offsetSecond(startTime, 5);


        // 定义触发器, 会马上执行一次, 接着1秒执行一次
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("testTrigger", "testTriggerGroup")
                .startNow()
                .startAt(startTime)
                .endAt(endTime)
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1))
                .build();

        // 使用触发器调度任务的执行
        scheduler.scheduleJob(job, trigger);

        // 开启任务
        scheduler.start();
    }
}
