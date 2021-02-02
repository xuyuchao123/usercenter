package com.xyc.userc.service;

/**
 * Created by 1 on 2021/2/1.
 */
public interface HallReportService
{
    int addReportInfo(String openId, String mobile, String carNum) throws Exception;

    int getWaitingNum(String openId) throws Exception;

    int getCurrentNum() throws Exception;
}
