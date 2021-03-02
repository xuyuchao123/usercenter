package com.xyc.userc.quartz.job;

import com.xyc.userc.service.HallReportService;
import com.xyc.userc.util.ApplicationContextHolder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 1 on 2021/3/2.
 */
public class HallReportQRCodeStrJob implements Job
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(HallReportQRCodeStrJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        LOGGER.info("开始执行更新玖厅报道二维码字符串定时任务");
        HallReportService hallReportService = (HallReportService) ApplicationContextHolder.getBean("hallReportService");
        try
        {
            hallReportService.refreshQRCodeStr();
        }
        catch (Exception e)
        {
            LOGGER.info("更新玖厅报道二维码字符串定时任务失败");
            e.printStackTrace();
        }
        LOGGER.info("更新玖隆大厅报道二维码字符串定时任务结束");
    }
}
