package com.sharkchili.quartzExample;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

@Slf4j
public class MyTriggerListener implements TriggerListener {
    private String name;

    public MyTriggerListener(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        String triggerName = trigger.getKey().getName();
       log.info(triggerName + " 被触发");
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        String triggerName = trigger.getKey().getName();
       log.info(triggerName + " 没有被触发");
        return false; // true：表示不会执行Job的方法
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        String triggerName = trigger.getKey().getName();
       log.info(triggerName + " 错过触发");
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, Trigger.CompletedExecutionInstruction completedExecutionInstruction) {
        String triggerName = trigger.getKey().getName();
       log.info(triggerName + " 完成之后触发");
    }

}
