package com.xyc.userc.service.impl;

import com.xyc.userc.dao.GarageMapper;
import com.xyc.userc.entity.GarageInfo;
import com.xyc.userc.service.GarageService;
import com.xyc.userc.util.BusinessException;
import com.xyc.userc.util.DataUtil;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.vo.GarageInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by 1 on 2021/1/26.
 */
@Service("garageService")
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class GarageServiceImpl implements GarageService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(GarageServiceImpl.class);


    @Autowired
    private GarageMapper garageMapper;


    @Override
    public List<GarageInfoVo> getGarageInfo(String garageType, String garageNum, String garageName) throws Exception
    {
        LOGGER.info("进入查询库位配置信息方法 garageType={} garageNum={} garageName={}",garageType,garageNum,garageName);
        List<GarageInfoVo> garageInfoVoList = garageMapper.selectGarageInfo(garageType,garageNum,garageName);
        LOGGER.info("结束查询库位配置信息方法 garageType={} garageNum={} garageName={}",garageType,garageNum,garageName);
        return garageInfoVoList;
    }

    @Override
    public void addGarageInfo(String garageType, String garageNum, String garageName, String maxLimit) throws Exception
    {
        LOGGER.info("进入新增库位配置信息方法 garageType={} garageNum={} garageName={} maxLimit={}",garageType,garageNum,garageName,maxLimit);
        Date date = new Date();
        if(!DataUtil.isNumeric(maxLimit))
        {
            LOGGER.error("最大限制量输入格式有误！");
            throw new BusinessException("最大限制量输入格式有误！");
        }
        GarageInfo garageInfo = new GarageInfo(null,garageType,garageNum,garageName,Integer.valueOf(maxLimit),date,date);
        List<GarageInfoVo> garageInfoVoList = garageMapper.selectGarageInfo(garageType,garageNum,garageName);
        if(garageInfoVoList == null || garageInfoVoList.size() == 0)
        {
            LOGGER.info("库位配置信息不存在，开始插入库位配置信息 garageType={} garageNum={} garageName={}",garageType,garageNum,garageName);
            garageMapper.insert(garageInfo);
        }
        else
        {
            LOGGER.info("库位配置信息已存在！garageType={} garageNum={} garageName={}",garageType,garageNum,garageName);
        }
        LOGGER.info("结束新增库位配置信息方法 garageType={} garageNum={} garageName={} maxLimit={}",garageType,garageNum,garageName,maxLimit);
    }

    @Override
    public void modifyGarageInfo(String garageNum, String maxLimit) throws Exception
    {
        LOGGER.info("进入修改库位配置信息方法 garageNum={} maxLimit={}",garageNum,maxLimit);
        if(!DataUtil.isNumeric(maxLimit))
        {
            LOGGER.error("最大限制量输入格式有误！");
            throw new BusinessException("最大限制量输入格式有误！");
        }
        Date date = new Date();
        List<GarageInfoVo> garageInfoVoList = garageMapper.selectGarageInfo(null,garageNum,null);
        if(garageInfoVoList == null || garageInfoVoList.size() == 0)
        {
            LOGGER.error("库位配置信息不存在！ garageNum={}",garageNum);
            throw new BusinessException(JsonResultEnum.GARAGEINFO_NOT_EXIST);
        }
        garageMapper.updateGarageInfo(garageNum,Integer.valueOf(maxLimit),date);
        LOGGER.info("结束修改库位配置信息方法 garageNum={} maxLimit={}",garageNum,maxLimit);
    }

    @Override
    public int removeGarageInfo(String garageNum) throws Exception
    {
        LOGGER.info("进入删除库位配置信息方法 garageNum={}",garageNum);
        List<GarageInfoVo> garageInfoVoList = garageMapper.selectGarageInfo(null,garageNum,null);
        if(garageInfoVoList == null || garageInfoVoList.size() == 0)
        {
            LOGGER.error("库位配置信息不存在！ garageNum={}",garageNum);
            throw new BusinessException(JsonResultEnum.GARAGEINFO_NOT_EXIST);
        }
        garageMapper.deleteByGarageNum(garageNum);
        LOGGER.info("结束删除库位配置信息方法 garageNum={}",garageNum);
        return 0;
    }

    @Override
    public int getMaxLimit(String garageType) throws Exception
    {
        LOGGER.info("开始查询库位最大限制量方法 garageType={}",garageType);
        List<GarageInfoVo> garageInfoVoList = garageMapper.selectGarageInfo(garageType,null,null);
        if(garageInfoVoList == null || garageInfoVoList.size() == 0)
        {
            LOGGER.error("库位配置信息不存在！ garageType={}",garageType);
            throw new BusinessException(JsonResultEnum.GARAGEINFO_NOT_EXIST);
        }
        int sumMaxLimit = 0;
        for(int i = 0; i < garageInfoVoList.size(); i++)
        {
            sumMaxLimit += garageInfoVoList.get(i).getMaxLimit();
        }
        LOGGER.info("结束查询库位最大限制量方法 garageType={} sumMaxLimit={}",garageType,sumMaxLimit);
        return sumMaxLimit;
    }


}
