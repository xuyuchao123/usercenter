package com.xyc.userc.quartz;

import com.xyc.userc.quartz.job.BlackListInOutJob;
import com.xyc.userc.quartz.job.CarNumViolationJob;
import com.xyc.userc.quartz.job.HallReportQRCodeStrJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * 定时任务调度处理
 * Created by 1 on 2020/11/4.
 */
@Configuration
public class MyQuartzScheduler
{
    @Autowired
    private Scheduler scheduler;

    /**
     * 开始执行所有任务
     *
     * @throws SchedulerException
     */
    public void startJob() throws SchedulerException
    {
        scheduleJob1(scheduler);
//        scheduleJob2(scheduler);
//        scheduleJob3(scheduler);
        scheduler.start();
    }

    /**
     * 获取Job信息
     *
     */
    public String getJobInfo(String name, String group) throws SchedulerException
    {
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        return String.format("time:%s,state:%s", cronTrigger.getCronExpression(),
                scheduler.getTriggerState(triggerKey).name());
    }

    /**
     * 修改某个任务的执行时间
     */
    public boolean modifyJob(String name, String group, String time) throws SchedulerException
    {
        Date date = null;
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldTime = cronTrigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(time))
        {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
                    .withSchedule(cronScheduleBuilder).build();
            date = scheduler.rescheduleJob(triggerKey, trigger);
        }
        return date != null;
    }

    /**
     * 暂停所有任务
     */
    public void pauseAllJob() throws SchedulerException
    {
        scheduler.pauseAll();
    }

    /**
     * 暂停某个任务
     */
    public void pauseJob(String name, String group) throws SchedulerException
    {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复所有任务
     */
    public void resumeAllJob() throws SchedulerException
    {
        scheduler.resumeAll();
    }

    /**
     * 恢复某个任务
     */
    public void resumeJob(String name, String group) throws SchedulerException
    {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除某个任务
     */
    public void deleteJob(String name, String group) throws SchedulerException
    {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
        {
            return;
        }
        scheduler.deleteJob(jobKey);
    }

    private void scheduleJob1(Scheduler scheduler) throws SchedulerException
    {
        // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例，JobDetail 是具体Job实例
        JobDetail jobDetail = JobBuilder.newJob(BlackListInOutJob.class).withIdentity("job1", "group1").build();
        // 基于表达式构建触发器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0/15 * * * ? *");
        // CronTrigger表达式触发器 继承于Trigger，TriggerBuilder 用于构建触发器实例
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("job1", "group1")
                .withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    private void scheduleJob2(Scheduler scheduler) throws SchedulerException
    {
        JobDetail jobDetail = JobBuilder.newJob(HallReportQRCodeStrJob.class).withIdentity("job2", "group2").build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0/1 * * * ? *");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("job2", "group2")
                .withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    //车牌号违章冻结自动任务
    private void scheduleJob3(Scheduler scheduler) throws SchedulerException
    {
        JobDetail jobDetail = JobBuilder.newJob(CarNumViolationJob.class).withIdentity("job3", "group3").build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0/10 * * * ? *");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("job3", "group3")
                .withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

}
