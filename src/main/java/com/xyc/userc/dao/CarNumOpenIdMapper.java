package com.xyc.userc.dao;

import com.xyc.userc.entity.CarNumOpenId;
import com.xyc.userc.entity.MobileOpenId;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 1 on 2020/8/18.
 */
public interface CarNumOpenIdMapper
{
    List<CarNumOpenId> selectByMobileCarNum(@Param("mobile")String mobile, @Param("carNum")String carNum);

}
