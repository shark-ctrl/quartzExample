package com.sharkchili.quartzExample;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

@Slf4j
public class MySchedulerListener implements SchedulerListener {
    @Override
    public void jobScheduled(Trigger trigger) {
        String jobName = trigger.getJobKey().getName();
       log.info(jobName + " 完成部署");
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
       log.info(triggerKey + " 完成卸载");
    }

    @Override
    public void triggerFinalized(Trigger trigger) {
       log.info("触发器被移除 " + trigger.getJobKey().getName());
    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {
       log.info(triggerKey + " 正在被暂停");
    }

    @Override
    public void triggersPaused(String triggerGroup) {
       log.info("触发器组 " + triggerGroup + " 正在被暂停");
    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {
       log.info(triggerKey + " 正在从暂停中恢复");
    }

    @Override
    public void triggersResumed(String triggerGroup) {
       log.info("触发器组 " + triggerGroup + " 正在从暂停中恢复");
    }

    @Override
    public void jobAdded(JobDetail jobDetail) {
       log.info(jobDetail.getKey() + " 添加工作任务");
    }

    @Override
    public void jobDeleted(JobKey jobKey) {
       log.info(jobKey + " 删除工作任务");
    }

    @Override
    public void jobPaused(JobKey jobKey) {
       log.info(jobKey + " 工作任务正在被暂停");
    }

    @Override
    public void jobsPaused(String jobGroup) {
       log.info("工作任务组 " + jobGroup + " 正在被暂停");
    }

    @Override
    public void jobResumed(JobKey jobKey) {
       log.info(jobKey + " 正在从暂停中恢复");
    }

    @Override
    public void jobsResumed(String jobGroup) {
       log.info("工作任务组 " + jobGroup + " 正在从暂停中恢复");
    }

    @Override
    public void schedulerError(String msg, SchedulerException cause) {
       log.info("产生严重错误时调用：   " + msg + "  " + cause.getUnderlyingException());
    }

    @Override
    public void schedulerInStandbyMode() {
       log.info("调度器在挂起模式下调用");
    }

    @Override
    public void schedulerStarted() {
       log.info("调度器 开启时调用");
    }

    @Override
    public void schedulerStarting() {
       log.info("调度器 正在开启时调用");
    }

    @Override
    public void schedulerShutdown() {
       log.info("调度器 已经被关闭 时调用");
    }

    @Override
    public void schedulerShuttingdown() {
       log.info("调度器 正在被关闭 时调用");
    }

    @Override
    public void schedulingDataCleared() {
       log.info("调度器的数据被清除时调用");
    }
}
