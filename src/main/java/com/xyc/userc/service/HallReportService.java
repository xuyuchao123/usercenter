package com.xyc.userc.service;

import com.xyc.userc.entity.HallReportInfo;
import com.xyc.userc.entity.QRCodeStrInfo;
import com.xyc.userc.vo.HallReportCommentVo;
import com.xyc.userc.vo.HallReportInfoVo;
import com.xyc.userc.vo.HallReportPrintQueueVo;

import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 2021/2/1.
 */
public interface HallReportService
{
    List<String> addReportInfo(String openId, String mobile, String carNum, String bigLadingBillNo) throws Exception;

    int getWaitingNum(String openId) throws Exception;

    int getCurrentNum() throws Exception;

    HallReportInfoVo getHallReportInfo(String openId) throws  Exception;

    HallReportCommentVo addHallReportComment(String openId, String carNum, String comment, String bigLadingBillNo) throws Exception;

    void refreshQRCodeStr() throws Exception;

    List<QRCodeStrInfo> getQRCodeStr() throws Exception;

    Map<String,List<HallReportPrintQueueVo>> getReportQueue() throws  Exception;
}
