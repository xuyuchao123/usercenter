package com.xyc.userc.dao;

import com.xyc.userc.entity.GarageInfo;
import com.xyc.userc.vo.GarageInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by 1 on 2021/1/26.
 */
public interface GarageMapper
{
    List<GarageInfoVo> selectGarageInfo(@Param("garageType")String garageType, @Param("garageNum")String garageNum,
                                        @Param("garageName")String garageName,@Param("location")String location);

    int insert(GarageInfo garageInfo);

    void updateGarageInfo(@Param("garageNum")String garageNum, @Param("maxLimit")Integer maxLimit, @Param("gmtModified")Date gmtModified);

    int deleteByGarageNum(String garageNum);
}
