package com.xyc.userc.dao;

import com.xyc.userc.entity.CarNumOpenId;
import com.xyc.userc.entity.MobileOpenId;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by 1 on 2020/8/18.
 */
public interface CarNumOpenIdMapper
{
    List<CarNumOpenId> selectByMobileCarNum(@Param("mobile")String mobile, @Param("carNum")String carNum);

    int deleteByCarNumOpenId(@Param("carNum")String carNum, @Param("openId")String openId);

    List<Integer> selectByOpenId(@Param("openId")String openId);

    int insert(CarNumOpenId carNumOpenId);

    void updateCarNum(@Param("oldCarNum")String oldCarNum, @Param("newCarNum")String newCarNum,
                      @Param("openId")String openId, @Param("gmtModified")Date gmtModified);

    int selectCntByCarNumOpenId(@Param("oldCarNum")String oldCarNum, @Param("openId")String openId);

}
