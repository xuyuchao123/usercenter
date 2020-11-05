package com.xyc.userc.dao;

import com.xyc.userc.entity.BlackListEnter;
import com.xyc.userc.entity.Blacklist;
import com.xyc.userc.vo.BlacklistVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 2020/8/24.
 */
public interface BlacklistMapper
{
    List<BlacklistVo> selectBlacklist(@Param("carNum")String carNum, @Param("createName")String createName,
                                      @Param("createMobile")String createMobile, @Param("start")int start, @Param("end")int end);

    int selectBlacklistCnt(@Param("carNum")String carNum,@Param("createName")String createName,@Param("createMobile")String createMobile);

    int insertBlacklist(Blacklist blacklist);

    List<Blacklist> selectByCarNum(String carNum);

    void deleteBlacklist(String carNum);

    List<BlackListEnter> selectBlacklistEnter();

    List<Map> selectBlackListInInfo(@Param("startTime")String startTime, @Param("endTime")String endTime);

    int insertBlacklistEnter(@Param("carNum")String carNum, @Param("enterDate")String enterDate);

    void updateCurDateValidStat(@Param("list")List<String> uptCurDateValidStatList, @Param("curDate")String curDate);

    void updateStatInvalid(@Param("list")List<String> uptStatInvalidList);
}
