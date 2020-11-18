package com.xyc.userc.service;

import com.xyc.userc.vo.ShipInfoVo;

import java.util.Date;
import java.util.List;

/**
 * Created by 1 on 2020/11/17.
 */
public interface ShipService
{
    void addShipReport(String shipNum, String name, String reportDepartment,
                       Date travelTimeStart, Date travelTimeEnd, String openId) throws Exception;

    int addShipInfo(String shipNum, String mobile, String cargoName, String name) throws Exception;

    int checkAndAddShipInfo(String shipNum, String mobile, String cargoName) throws Exception;

    List<ShipInfoVo> queryShipInfo(String id) throws Exception;


}
