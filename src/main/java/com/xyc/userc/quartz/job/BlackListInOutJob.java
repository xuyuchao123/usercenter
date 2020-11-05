package com.xyc.userc.quartz.job;

import com.xyc.userc.service.BlacklistService;
import com.xyc.userc.service.UserService;
import com.xyc.userc.util.MyCommandLineRunner;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Created by 1 on 2020/11/4.
 */
public class BlackListInOutJob implements Job
{
    @Resource
    BlacklistService blacklistService;

    protected static final Logger LOGGER = LoggerFactory.getLogger(BlackListInOutJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        LOGGER.info("开始执行查询黑名单车辆进场情况定时任务");
        try
        {
            blacklistService.refreshBlackListEnter();
        }
        catch (Exception e)
        {
            LOGGER.info("查询黑名单车辆进场情况定时任务失败");
            e.printStackTrace();
        }
    }
}
