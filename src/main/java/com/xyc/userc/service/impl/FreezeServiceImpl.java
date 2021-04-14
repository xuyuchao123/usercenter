package com.xyc.userc.service.impl;

import com.xyc.userc.dao.FreezeMapper;
import com.xyc.userc.dao2.CarNumViolationMapper;
import com.xyc.userc.entity.CarNumFrozen;
import com.xyc.userc.service.FreezeService;
import com.xyc.userc.util.*;
import com.xyc.userc.vo.CarNumFrozenAddVo;
import com.xyc.userc.vo.CarNumFrozenVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by 1 on 2021/4/13.
 */
@Service("freezeService")
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class FreezeServiceImpl implements FreezeService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(FreezeServiceImpl.class);

    @Autowired
    private FreezeMapper freezeMapper;

    @Autowired
    private CarNumViolationMapper carNumViolationMapper;

    @Override
    public List queryCarNumFrozen(String carNum, String frozenStatus, String page, String size) throws Exception
    {
        LOGGER.info("进入查询车牌号违章冻结信息方法");
        Integer frozenStatusInt = null;
        if(frozenStatus != null && (frozenStatus.equals("0") || frozenStatus.equals("1")))
        {
            frozenStatusInt = Integer.valueOf(frozenStatus);
        }
        List<CarNumFrozenVo> carNumFrozenVos = freezeMapper.selectCarNumFrozenVo(carNum,frozenStatusInt);
        List resList = PageUtil.getPageInfoList(carNumFrozenVos,page,size);
        LOGGER.info("结束查询车牌号违章冻结信息方法");
        return resList;
    }

    @Override
    public void freezeCarNum(List<CarNumFrozenAddVo> carNumFrozenAddVos) throws Exception
    {
        LOGGER.info("进入冻结车牌号方法");
        List<String> carNums = new ArrayList<>();
        for(CarNumFrozenAddVo carNumFrozenAddVo : carNumFrozenAddVos)
        {
            carNums.add(carNumFrozenAddVo.getCarNum());
        }
        LOGGER.info("要冻结的车牌列表：{}",carNums.toArray().toString());
        List<CarNumFrozen> carNumFrozens = freezeMapper.selectByCarNums(carNums);
        Map<String,CarNumFrozen> carNumFrozStatMap = new HashMap<>();
        for(CarNumFrozen carNumFrozen : carNumFrozens)
        {
            carNumFrozStatMap.put(carNumFrozen.getCarNum(),carNumFrozen);
        }
        List<CarNumFrozen> uptList = new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = DateUtils.localDateTime2Date(localDateTime);
        for(String carNum : carNums)
        {
            CarNumFrozen tmpCarNumFrozen = carNumFrozStatMap.get(carNum);
            if(tmpCarNumFrozen == null)
            {
                LOGGER.error("车牌号不存在 carNum={}",carNum);
                throw new BusinessException(JsonResultEnum.CARNUM_NOT_EXIST);
            }
            if(tmpCarNumFrozen.getFrozenStatus() == 1)
            {
                LOGGER.error("车牌号已在冻结状态 carNum={}",carNum);
                throw new BusinessException(JsonResultEnum.CARNUM_FROZEN);
            }
            tmpCarNumFrozen.setStartDate(date);
            Date expireDate = DateUtils.localDateTime2Date(localDateTime.plusDays(tmpCarNumFrozen.getViolationTimes().intValue() * UsercConstant.FROZEN_PERIOD));
            tmpCarNumFrozen.setExpireDate(expireDate);
            tmpCarNumFrozen.setFrozenStatus(1);
            tmpCarNumFrozen.setGmtModified(date);
            uptList.add(tmpCarNumFrozen);
        }
        freezeMapper.updateCarNumFrozens(uptList);
        LOGGER.info("结束冻结车牌号方法");

    }

    @Override
    public void unFreezeCarNum(List<String> carNums) throws Exception
    {
        LOGGER.info("开始解冻车牌号方法");
        List<CarNumFrozen> carNumFrozens = freezeMapper.selectByCarNums(carNums);
        Map<String,Integer> carNumFrozStatMap = new HashMap<>();
        for(CarNumFrozen carNumFrozen : carNumFrozens)
        {
            carNumFrozStatMap.put(carNumFrozen.getCarNum(),carNumFrozen.getFrozenStatus());
        }
        for(String carNum : carNums)
        {
            if(carNumFrozStatMap.get(carNum) == null)
            {
                LOGGER.error("车牌号不存在 carNum={}",carNum);
                throw new BusinessException(JsonResultEnum.CARNUM_NOT_EXIST);
            }
            if(carNumFrozStatMap.get(carNum).intValue() == 0)
            {
                LOGGER.error("车牌号已在解冻状态 carNum={}",carNum);
                throw new BusinessException(JsonResultEnum.CARNUM_FROZEN);
            }
        }
        freezeMapper.updateCarNumUnFrozens(carNums);
        LOGGER.info("结束解冻车牌号方法");
    }

    @Override
    public void refreshCarNumViolation() throws Exception
    {
        LOGGER.info("进入更新车牌号违章情况方法");
        List<CarNumFrozen> carNumFrozens = freezeMapper.selectCarNumFrozen(null,null);
        Map<String,CarNumFrozen> frozenMap = new HashMap<>();
        for(CarNumFrozen carNumFrozen : carNumFrozens)
        {
            frozenMap.put(carNumFrozen.getCarNum(),carNumFrozen);
        }
        //从安全系统获取车辆A类违章信息
        List<Map> violationInfoList = carNumViolationMapper.selectCarNumAViolationInfo();

        //测试数据
//        Date date1 = new Date(1619168786000l);
//        Date date2 = new Date(1619168882000l);
//        Date date3 = new Date(1619168782000l);
//        Date date4 = new Date(1619168732000l);
//        Date date5 = new Date(1619162782000l);
//        Date date6 = new Date(1619168789000l);
//        Date date7 = new Date(1619168389000l);
//        List<Map> violationInfoList = new ArrayList<>();
//        Map map1 = new HashMap();
//        map1.put("carNum","aaaa");
//        map1.put("lastViolationTime",date1);
//        violationInfoList.add(map1);
//
//        Map map2 = new HashMap();
//        map2.put("carNum","aaaa");
//        map2.put("lastViolationTime",date2);
//        violationInfoList.add(map2);
//
//        Map map3 = new HashMap();
//        map3.put("carNum","aaaa");
//        map3.put("lastViolationTime",date3);
//        violationInfoList.add(map3);
//
//        Map map4 = new HashMap();
//        map4.put("carNum","kkkk");
//        map4.put("lastViolationTime",date4);
//        violationInfoList.add(map4);
//
//        Map map5 = new HashMap();
//        map5.put("carNum","kkkk");
//        map5.put("lastViolationTime",date5);
//        violationInfoList.add(map5);
//
//        Map map6 = new HashMap();
//        map6.put("carNum","lll");
//        map6.put("lastViolationTime",date6);
//        violationInfoList.add(map6);
//
//        Map map7 = new HashMap();
//        map7.put("carNum","mmm");
//        map7.put("lastViolationTime",date7);
//        violationInfoList.add(map7);

        List<Map> groupList = new ArrayList<>();
        int tmpCnt = 0;
        String preCarNum = null;
        Map<String,Object> preMap = new HashMap<>();
        for(int i = 0; i < violationInfoList.size(); i++)
        {
            Map map = violationInfoList.get(i);
            if(i == 0)
            {
                preCarNum = map.get("carNum").toString();
                Map<String,Object> tmpMap = new HashMap<>();
                tmpMap.put("carNum",map.get("carNum").toString());
                tmpMap.put("violationTimes",++tmpCnt);
                tmpMap.put("lastViolationTime",(Date)map.get("lastViolationTime"));        //lastViolationTime类型待确认
                groupList.add(tmpMap);
                preMap = tmpMap;
            }
            else
            {
                if(preCarNum.equals(map.get("carNum").toString()))
                {
                    preMap.put("violationTimes",++tmpCnt);
                }
                else
                {
                    tmpCnt = 0;
                    Map<String,Object> tmpMap = new HashMap<>();
                    tmpMap.put("carNum",map.get("carNum").toString());
                    tmpMap.put("violationTimes",++tmpCnt);
                    tmpMap.put("lastViolationTime",(Date)map.get("lastViolationTime"));
                    groupList.add(tmpMap);
                    preMap = tmpMap;
                    preCarNum = map.get("carNum").toString();
                }
            }
        }

        //需要添加违章记录的车牌列表
        List<CarNumFrozen> insertList = new ArrayList<>();
        //需要修改违章记录的车牌列表
        List<CarNumFrozen> updateList = new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = DateUtils.localDateTime2Date(localDateTime);
        if(groupList.size()>0)
        {
            for (Map map : groupList)
            {
                String loopCarNum = map.get("carNum").toString();
                int loopTimes = (Integer)map.get("violationTimes");
                Date LastViolationTime = (Date)map.get("lastViolationTime");    //lastViolationTime类型转换待确认
                CarNumFrozen tmpCarNumFrozen = frozenMap.get(loopCarNum);
                if(tmpCarNumFrozen == null)     //当前车牌号没有冻结记录
                {
                    CarNumFrozen instCarNumFrozen = new CarNumFrozen(null,loopCarNum,0,null,null,date,date,loopTimes,LastViolationTime);
                    insertList.add(instCarNumFrozen);
                }
                else                            //当前车牌号有冻结记录
                {
                    tmpCarNumFrozen.setViolationTimes(loopTimes);
                    tmpCarNumFrozen.setGmtModified(date);
                    tmpCarNumFrozen.setLastViolationTime(LastViolationTime);
                    //判断当前记录是否要解冻
                    if(date.compareTo(tmpCarNumFrozen.getExpireDate()) >= 0)
                    {
                        tmpCarNumFrozen.setFrozenStatus(0);
                    }
                    updateList.add(tmpCarNumFrozen);
                    frozenMap.remove(loopCarNum);
                }
            }
        }
        Set<String> keySet = frozenMap.keySet();
        for(String key : keySet)
        {
            CarNumFrozen tmpCarNumFrozen2 = frozenMap.get(key);
            if(date.compareTo(tmpCarNumFrozen2.getExpireDate()) >= 0)      //当前日期大于等于冻结到期日，需要解冻
            {
                tmpCarNumFrozen2.setFrozenStatus(0);
                tmpCarNumFrozen2.setGmtModified(date);
                updateList.add(tmpCarNumFrozen2);
            }
        }
        if(updateList.size() > 0)
        {
            freezeMapper.updateAllCarNumFrozen(updateList);
        }
        if(insertList.size() > 0)
        {
            freezeMapper.insertAllCarNumFrozen(insertList);
        }
        LOGGER.info("结束更新车牌号违章情况方法");

    }


}
