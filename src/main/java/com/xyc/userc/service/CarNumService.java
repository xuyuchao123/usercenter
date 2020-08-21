package com.xyc.userc.service;

import com.xyc.userc.entity.CarNumOpenId;

import java.util.List;

/**
 * Created by 1 on 2020/8/21.
 */
public interface CarNumService
{
    List<CarNumOpenId> getCarNum(String mobile, String carNum) throws Exception;

    void removeCarNum(String carNum, String openId) throws Exception;

    void addCarNum(String carNum, String openId) throws Exception;

    void modifyCarNumByOpenId(String oldCarNum, String newCarNum, String openId) throws Exception;
}
