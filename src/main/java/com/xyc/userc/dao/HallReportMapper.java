package com.xyc.userc.dao;

import com.xyc.userc.entity.HallReportComment;
import com.xyc.userc.entity.HallReportInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 1 on 2021/2/1.
 */
public interface HallReportMapper
{
    int insert(HallReportInfo hallReportInfo);

    List<HallReportInfo> selectHallReportInfo(@Param("openId")String openId, @Param("mobile")String mobile, @Param("carNum")String carNum, @Param("dataStatus")Integer dataStatus);

    int selectWaitingNum(String openId);

    int selectCurrentNum();

    void insertComment(HallReportComment hallReportComment);
}
