package com.xyc.userc.service;

import com.xyc.userc.entity.CarNumOpenId;
import com.xyc.userc.vo.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 2020/8/21.
 */
public interface CarNumService
{
    List<CarNumOpenIdVo> getCarNum(String openId) throws Exception;

    void removeCarNum(String carNum, String openId) throws Exception;

    void addCarNum(String carNum, String openId, String engineNum, String identNum,
                   String emissionStd, String fleetName, Date regDate, String department,String drivingLicense) throws Exception;

    void modifyCarNumByOpenId(String oldCarNum, String newCarNum, String engineNum, String identNum,
                              String emissionStd, String fleetName, Date regDate, String department,
                              String drivingLicense,String openId) throws Exception;

    void enableCarNum(String carNum, String openId) throws Exception;

    List<CarNumInOutTimeVo> queryInOutTime(String openId) throws Exception;

    List<GsCarInfoVo> queryGsCarInfo(String carNum) throws Exception;

    List queryEnvInfo(String carNum, String startDate, String page, String size) throws Exception;

    boolean queryDrivinglicense(String carNum) throws Exception;


}
