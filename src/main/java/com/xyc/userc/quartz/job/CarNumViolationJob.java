package com.xyc.userc.quartz.job;

import com.xyc.userc.service.FreezeService;
import com.xyc.userc.util.ApplicationContextHolder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 1 on 2021/4/8.
 */
public class CarNumViolationJob implements Job
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(CarNumViolationJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
       LOGGER.info("开始执行更新车牌号冻结情况定时任务");
        FreezeService freezeService = (FreezeService) ApplicationContextHolder.getBean("freezeService");
        try
        {
            freezeService.refreshCarNumViolation();
        }
        catch (Exception e)
        {
            LOGGER.info("更新车牌号冻结情况定时任务失败");
            e.printStackTrace();
        }
        LOGGER.info("结束执行更新车牌号冻结情况定时任务");

    }
}
