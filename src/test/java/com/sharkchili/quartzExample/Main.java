package com.sharkchili.quartzExample;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.EverythingMatcher;

import java.util.Date;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        jobListenerExample();
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


    /**
     * 设置CronTrigger
     * 表达式可到 https://cron.qqe2.com/
     *
     * @throws SchedulerException
     */
    public static void cronTriggerExample() throws SchedulerException {
        // 获取任务调度的实例
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 定义任务调度实例, 并与TestJob绑定
        JobDetail job = JobBuilder.newJob(MyJob.class)
                .withIdentity("myJob", "myJobGroup")
                .usingJobData("count", 1)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("testTrigger", "testTriggerGroup")
                .withSchedule(CronScheduleBuilder.cronSchedule("* * * * * ?"))
                .build();

        // 使用触发器调度任务的执行 获取任务调度时间
        Date date =scheduler.scheduleJob(job, trigger);

        log.info("任务调度时间:{}", date);


        // 开启任务
        scheduler.start();

        /**
         * scheduler.start();
         * 将任务调度挂起（暂停）：
         *
         * scheduler.standby();
         * 将任务关闭：
         *
         * shutdown(true);//表示等待所有正在执行的job执行完毕之后，再关闭Scheduler
         * shutdown(false);//表示直接关闭Scheduler
         */

        // 创建并注册一个全局的Job Listener
        scheduler.shutdown(false);

    }





    /**
     * 设置任务监听
     * @throws SchedulerException
     */
    public static void jobListenerExample() throws SchedulerException {
        // 获取任务调度的实例
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 定义任务调度实例, 并与TestJob绑定
        JobDetail job = JobBuilder.newJob(MyJob.class)
                .withIdentity("myJob", "myJobGroup")
                .usingJobData("count", 1)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("testTrigger", "testTriggerGroup")
                .withSchedule(CronScheduleBuilder.cronSchedule("* * * * * ?"))
                .build();

        // 使用触发器调度任务的执行 获取任务调度时间
        Date date =scheduler.scheduleJob(job, trigger);

        log.info("任务调度时间:{}", date);




        /**
         * scheduler.start();
         * 将任务调度挂起（暂停）：
         *
         * scheduler.standby();
         * 将任务关闭：
         *
         * shutdown(true);//表示等待所有正在执行的job执行完毕之后，再关闭Scheduler
         * shutdown(false);//表示直接关闭Scheduler
         */

        // 创建并注册一个全局的Job Listener
        scheduler.getListenerManager()
                .addJobListener(new MyJobListener(),
                        EverythingMatcher.allJobs());

        // 创建并注册一个全局的Trigger Listener
        scheduler.getListenerManager().addTriggerListener(new MyTriggerListener("simpleTrigger"), EverythingMatcher.allTriggers());

        // 创建SchedulerListener
        scheduler.getListenerManager().addSchedulerListener(new MySchedulerListener());

        // 开启任务
        scheduler.start();

    }


}
