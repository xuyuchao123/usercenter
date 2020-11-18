package com.xyc.userc.service.impl;

import com.xyc.userc.dao.ShipMapper;
import com.xyc.userc.entity.ShipInfo;
import com.xyc.userc.entity.ShipReport;
import com.xyc.userc.service.ShipService;
import com.xyc.userc.util.BusinessException;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.vo.ShipInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by 1 on 2020/11/17.
 */
@Service("shipService")
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class ShipServiceImpl implements ShipService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(ShipServiceImpl.class);

    @Autowired
    private ShipMapper shipMapper;


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
        shipMapper.insertShipReport(shipReport);
        LOGGER.info("结束新增报港数据方法 shipNum={},name={},reportDepartment={},travelTimeStart={},travelTimeEnd={}",
                shipNum,name,reportDepartment,travelTimeStart.toInstant(),travelTimeEnd.toString());
    }

    @Override
    public int addShipInfo(String shipNum, String mobile, String cargoName, String name) throws Exception
    {
        LOGGER.info("进入保存废钢处船户信息方法: shipNum:{} mobile:{} cargoName:{} name:{}",shipNum,mobile,cargoName,name);
        ShipInfo shipInfo = new ShipInfo();
        shipInfo.setShipNum(shipNum);
        shipInfo.setMobile(mobile);
        shipInfo.setCargoName(cargoName);
        shipInfo.setName(name);
        Date date = new Date();
        shipInfo.setGmtCreate(date);
        shipInfo.setGmtModified(date);
        int id = shipMapper.insert(shipInfo);
        LOGGER.info("保存废钢处船户信息成功，返回id号 ：{}",id);
        LOGGER.info("结束保存废钢处船户信息方法: shipNum:{} mobile:{} cargoName:{} name:{}",shipNum,mobile,cargoName,name);
        return id;
    }

    @Override
    public int checkAndAddShipInfo(String shipNum, String mobile, String cargoName) throws Exception
    {
        LOGGER.info("进入审核并保存海力物流船户信息方法: shipNum:{} mobile:{} cargoName:{}",shipNum,mobile,cargoName);
        int cnt = shipMapper.checkShipInfo(shipNum,mobile);
        int id = 0;
        if(cnt > 0)
        {
            LOGGER.info("审核通过,开始存入船户信息 cnt：{}",cnt);
            ShipInfo shipInfo = new ShipInfo();
            shipInfo.setShipNum(shipNum);
            shipInfo.setMobile(mobile);
            shipInfo.setCargoName(cargoName);
            shipInfo.setName("");
            Date date = new Date();
            shipInfo.setGmtCreate(date);
            shipInfo.setGmtModified(date);
            id = shipMapper.insert(shipInfo);
            LOGGER.info("保存海力物流船户信息成功，返回id号 ：{}",id);
        }
        else
        {
            LOGGER.info("审核失败 cnt：{}",cnt);
            throw new BusinessException(JsonResultEnum.SHIPINFO_CHECK_FAIL);
        }
        LOGGER.info("结束审核并保存海力物流船户信息方法: shipNum:{} mobile:{} cargoName:{}",shipNum,mobile,cargoName);
        return id;
    }

    @Override
    public List<ShipInfoVo> queryShipInfo(String id) throws Exception
    {
        LOGGER.info("进入查询船户信息方法: id:{}",id);
        List<ShipInfoVo> shipInfoVos = shipMapper.selectById(Integer.valueOf(id));
        LOGGER.info("结束查询船户信息方法: id:{}",id);
        return shipInfoVos;
    }


}
