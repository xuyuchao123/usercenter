package com.xyc.userc.dao;

import com.xyc.userc.entity.HallReportComment;
import com.xyc.userc.entity.HallReportInfo;
import com.xyc.userc.entity.QRCodeStrInfo;
import com.xyc.userc.vo.HallReportPrintQueueVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 2021/2/1.
 */
public interface HallReportMapper
{
    int insert(HallReportInfo hallReportInfo);

    List<String> selectBigLadingBillNo(@Param("carNum")String carNum, @Param("mobile")String mobile, @Param("timeStr")String timeStr);

    List<HallReportInfo> selectHallReportInfo(@Param("openId")String openId, @Param("mobile")String mobile, @Param("carNum")String carNum,
                                              @Param("bigLadingBillNo")String bigLadingBillNo,@Param("dataStatus")Integer dataStatus);

    List<Map> selectWaitingNum(String openId);

    List<Integer> selectCurrentNum();

    void insertComment(HallReportComment hallReportComment);

    int deleteEarliestQRCodeStr();

    int insertQRCodeStr(QRCodeStrInfo qrCodeStrInfo);

    List<QRCodeStrInfo> selectQRCodeStr();

    List<HallReportPrintQueueVo> selectReportPrintQueue();
}
