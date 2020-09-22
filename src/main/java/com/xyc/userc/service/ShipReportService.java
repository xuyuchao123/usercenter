package com.xyc.userc.service;

import com.xyc.userc.entity.ShipReport;

import java.util.Date;

/**
 * Created by 1 on 2020/9/22.
 */
public interface ShipReportService
{
    void addShipReport(String shipNum, String name, String reportDepartment,
                       Date travelTimeStart, Date travelTimeEnd, String openId) throws Exception;
}
