package com.xyc.userc.service.impl;

import com.xyc.userc.dao.ShipReportMapper;
import com.xyc.userc.dao.UserMapper;
import com.xyc.userc.entity.MobileOpenId;
import com.xyc.userc.entity.ShipReport;
import com.xyc.userc.service.ShipReportService;
import com.xyc.userc.util.BusinessException;
import com.xyc.userc.util.JsonResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by 1 on 2020/9/22.
 */
@Service("shipReportService")
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class ShipReportServiceImpl implements ShipReportService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(ShipReportServiceImpl.class);

    @Autowired
    private ShipReportMapper shipReportMapper;


    @Override
    public void addShipReport(String shipNum, String name, String reportDepartment,
                              Date travelTimeStart, Date travelTimeEnd, String openId) throws Exception
    {
        LOGGER.info("进入新增报港数据方法 shipNum={},name={},reportDepartment={},travelTimeStart={},travelTimeEnd={}",
                shipNum,name,reportDepartment,travelTimeStart.toString(),travelTimeEnd.toString());
        Date date = new Date();
//        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ShipReport shipReport = new ShipReport(null,shipNum,name,reportDepartment,travelTimeStart,travelTimeEnd,0,
                openId,date,openId,date,0);
        shipReportMapper.insertShipReport(shipReport);
        LOGGER.info("结束新增报港数据方法 shipNum={},name={},reportDepartment={},travelTimeStart={},travelTimeEnd={}",
                shipNum,name,reportDepartment,travelTimeStart.toInstant(),travelTimeEnd.toString());
    }
}
