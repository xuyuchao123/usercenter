package com.xyc.userc.service;

import com.xyc.userc.vo.GarageInfoVo;

import java.util.List;

/**
 * Created by 1 on 2021/1/26.
 */
public interface GarageService
{
    List<GarageInfoVo> getGarageInfo(String garageType, String garageNum, String garageName) throws Exception;

    void addGarageInfo(String garageType, String garageNum, String garageName,String maxLimit) throws Exception;

    void modifyGarageInfo(String garageNum,String maxLimit) throws Exception;

    int removeGarageInfo(String garageNum) throws Exception;

    int getMaxLimit(String garageType) throws Exception;

}
