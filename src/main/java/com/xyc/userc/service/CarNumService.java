package com.xyc.userc.service;

import com.xyc.userc.entity.CarNumOpenId;
import com.xyc.userc.vo.CarNumInOutTimeVo;
import com.xyc.userc.vo.GsCarInfoVo;

import java.util.List;

/**
 * Created by 1 on 2020/8/21.
 */
public interface CarNumService
{
    List<CarNumOpenId> getCarNum(String mobile, String carNum) throws Exception;

    void removeCarNum(String carNum, String openId) throws Exception;

    void addCarNum(String carNum, String openId, String engineNum, String identNum,String emissionStd) throws Exception;

    void modifyCarNumByOpenId(String oldCarNum, String newCarNum, String engineNum,
                              String identNum, String emissionStd, String openId) throws Exception;

    void enableCarNum(String carNum, String openId) throws Exception;

    List<CarNumInOutTimeVo> queryInOutTime(String openId) throws Exception;

    List<GsCarInfoVo> queryGsCarInfo(String carNum) throws Exception;
}
