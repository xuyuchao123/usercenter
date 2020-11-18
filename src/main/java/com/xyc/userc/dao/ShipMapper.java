package com.xyc.userc.dao;

import com.xyc.userc.entity.ShipInfo;
import com.xyc.userc.entity.ShipReport;
import com.xyc.userc.vo.ShipInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 1 on 2020/11/17.
 */
public interface ShipMapper
{
    int insertShipReport(ShipReport shipReport);

    int insert(ShipInfo shipInfo);

    int checkShipInfo(@Param("shipNum")String shipNum, @Param("mobile")String mobile);

    List<ShipInfoVo> selectById(Integer id);
}
