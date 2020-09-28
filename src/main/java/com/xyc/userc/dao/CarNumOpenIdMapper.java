package com.xyc.userc.dao;

import com.xyc.userc.entity.CarNumOpenId;
import com.xyc.userc.entity.MobileOpenId;
import com.xyc.userc.vo.CarNumInOutTimeVo;
import com.xyc.userc.vo.CarNumInfoVo;
import com.xyc.userc.vo.EnabledCarInfoVo;
import com.xyc.userc.vo.GsCarInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 2020/8/18.
 */
public interface CarNumOpenIdMapper
{
    List<CarNumOpenId> selectByMobileCarNum(@Param("mobile")String mobile, @Param("carNum")String carNum);

    int deleteByCarNumOpenId(@Param("carNum")String carNum, @Param("openId")String openId);

    List<Integer> selectMobileOpenIdIdByOpenId(@Param("openId")String openId);

    int insert(CarNumOpenId carNumOpenId);

    void updateCarNum(@Param("oldCarNum")String oldCarNum, @Param("newCarNum")String newCarNum,
                      @Param("engineNum")String engineNum, @Param("identNum")String identNum,
                      @Param("emissionStd")String emissionStd, @Param("openId")String openId, @Param("gmtModified")Date gmtModified);

    void updateCarNumEnable(@Param("isEnable")Integer isEnable, @Param("gmtModified")Date gmtModified,
                                @Param("carNum")String carNum, @Param("openId")String openId);

    int selectCntByCarNumOpenId(@Param("oldCarNum")String oldCarNum, @Param("openId")String openId);

    List<CarNumOpenId> selectByOpenIdCarNum(@Param("openId")String openId, @Param("carNum")String carNum);

    List<CarNumInOutTimeVo> selectCarNumInOutTime(@Param("openId")String openId, @Param("startTime")String startTime,
                                                    @Param("endTime")String endTime);

    List<GsCarInfoVo> selectGsCarInfoByCarNum(@Param("carNum")String carNum);

    EnabledCarInfoVo selectEnabledCarInfo(@Param("openId")String openId);

    List selectCarNumInfo();

}
