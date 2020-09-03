package com.xyc.userc.service.impl;

import com.xyc.userc.dao.*;
import com.xyc.userc.entity.CarNumOpenId;
import com.xyc.userc.service.CarNumService;
import com.xyc.userc.util.BusinessException;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.RoleTypeEnum;
import com.xyc.userc.vo.CarNumInOutTimeVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * Created by 1 on 2020/8/21.
 */
@Service("carNumService")
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class CarNumServiceImpl implements CarNumService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(CarNumServiceImpl.class);


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private CarNumOpenIdMapper carNumOpenIdMapper;


    @Override
    public List<CarNumOpenId> getCarNum(String mobile, String carNum) throws Exception
    {
        LOGGER.info("进入查询车牌号方法 mobile:{} carNum：{}",mobile,carNum);
        List<CarNumOpenId> carNumOpenIdList = carNumOpenIdMapper.selectByMobileCarNum(mobile,carNum);
        LOGGER.info("结束查询车牌号方法mobile:{} carNum：{}",mobile,carNum);
        return carNumOpenIdList;
    }

    @Override
    public void removeCarNum(String carNum, String openId) throws Exception
    {
        LOGGER.info("进入删除车牌号方法carNum={} openId={}",carNum,openId);
        List<Integer> mobileOpenIdIdList = carNumOpenIdMapper.selectMobileOpenIdIdByOpenId(openId);
        int deleteCnt = carNumOpenIdMapper.deleteByCarNumOpenId(carNum,openId);
        if(deleteCnt > 0)
        {
            if(mobileOpenIdIdList == null || mobileOpenIdIdList.size() == 0)
            {
                LOGGER.info("删除成功，当前用户没有已绑定的车牌号，准备修改其角色carNum={} openId={}",carNum,openId);
                //获取角色"司机0"的id
                int roleId = roleMapper.selectIdByRoleCode(RoleTypeEnum.ROLE_SJ_0.getRoleCode());
                //更新当前用户的角色为司机0
                userMapper.updateRoleIdByMobileOpenId(roleId,mobileOpenIdIdList.get(0));
                LOGGER.info("角色修改成功carNum={} openId={}",carNum,openId);
            }
        }
        else
        {
            LOGGER.info("当前用户没有绑定该车牌号,删除失败！carNum={} openId={}",carNum,openId);
            throw new BusinessException(JsonResultEnum.CARNUM_NOT_BINDED);
        }

        LOGGER.info("结束删除车牌号方法carNum={} openId={}",carNum,openId);
    }

    @Override
    public void addCarNum(String carNum, String openId) throws Exception
    {
        LOGGER.info("进入新增车牌号方法carNum={} openId={}",carNum,openId);
        LOGGER.info("开始检查车牌号是否已被绑定carNum={} openId={}",carNum,openId);
        int cnt = carNumOpenIdMapper.selectCntByCarNumOpenId(carNum,null);
        Date date = new Date();
        CarNumOpenId carNumOpenId = new CarNumOpenId(null,openId,carNum,0,0,openId,openId,date,date);
        if(cnt > 0)
        {
            LOGGER.info("该车牌号已被绑定，不能重复绑定 carNum={} openId={}",carNum,openId);
            throw new BusinessException(JsonResultEnum.CARNUM_BINDED);
        }
        int insertCnt = carNumOpenIdMapper.insert(carNumOpenId);
        List<Integer> mobileOpenIdIdList = carNumOpenIdMapper.selectMobileOpenIdIdByOpenId(openId);
        if(insertCnt > 0 && (mobileOpenIdIdList != null && mobileOpenIdIdList.size() == 1))
        {
            LOGGER.info("新增车牌成功，当前用户仅绑定一个车牌号，准备修改其角色carNum={} openId={}",carNum,openId);
            //获取角色"司机1"的id
            int roleId = roleMapper.selectIdByRoleCode(RoleTypeEnum.ROLE_SJ_1.getRoleCode());
            //更新当前用户的角色为司机1
            userMapper.updateRoleIdByMobileOpenId(roleId,mobileOpenIdIdList.get(0));
            LOGGER.info("角色修改成功carNum={} openId={}",carNum,openId);
        }
        LOGGER.info("结束新增车牌号方法 carNum={} openId={}",carNum,openId);
    }

    @Override
    public void modifyCarNumByOpenId(String oldCarNum, String newCarNum, String openId) throws Exception
    {
        LOGGER.info("进入修改车牌号方法 oldCarNum={} newCarNum={} openId={}",oldCarNum,newCarNum,openId);
        int selectCnt = carNumOpenIdMapper.selectCntByCarNumOpenId(oldCarNum,openId);
        if(selectCnt == 1)
        {
            carNumOpenIdMapper.updateCarNum(oldCarNum,newCarNum,openId,new Date());
            LOGGER.info("成功修改车牌号 oldCarNum={} newCarNum={} openId={}",oldCarNum,newCarNum,openId);
        }
        else
        {
            LOGGER.info("该用户未绑定该车牌，修改失败 oldCarNum={} newCarNum={} openId={}",oldCarNum,newCarNum,openId);
            throw new BusinessException(JsonResultEnum.CARNUM_NOT_BINDED);
        }
        LOGGER.info("结束修改车牌号方法  oldCarNum={} newCarNum={} openId={}",oldCarNum,newCarNum,openId);
    }

    @Override
    public void enableCarNum(String carNum, String openId) throws Exception
    {
        LOGGER.info("进入启用车牌号方法 CarNum={} openId={}",carNum,openId);
        List<CarNumOpenId> carNumOpenIdList = carNumOpenIdMapper.selectByOpenIdCarNum(null,carNum);
        if(carNumOpenIdList == null || carNumOpenIdList.size() == 0)
        {
            LOGGER.info("该车牌号已删除或不存在 CarNum={} openId={}",carNum,openId);
            throw new BusinessException(JsonResultEnum.CARNUM_NOT_EXIST);
        }
        CarNumOpenId carNumOpenId = carNumOpenIdList.get(0);
        if(!carNumOpenId.getOpenId().equals(openId))
        {
            LOGGER.info("当前用户未绑定该车牌 CarNum={} openId={}",carNum,openId);
            throw new BusinessException(JsonResultEnum.CARNUM_NOT_BINDED);
        }
        if(carNumOpenId.getIsEnabled() == 1)
        {
            LOGGER.info("该车牌号已在启用状态不能重复启用 CarNum={} openId={}",carNum,openId);
            throw new BusinessException(JsonResultEnum.CARNUM_ENABLED);
        }
//        List<CarNumOpenId> carNumOpenIdList2 = carNumOpenIdMapper.selectByOpenIdCarNum(openId,null,1);
        carNumOpenIdMapper.updateCarNumEnable(0,new Date(),null,openId);
        carNumOpenIdMapper.updateCarNumEnable(1,new Date(),carNumOpenId.getCarNum(),openId);
        LOGGER.info("成功启用车牌号 carNum={} openId={}", carNum,openId);
        LOGGER.info("结束启用车牌号方法 CarNum={} openId={}",carNum,openId);
    }

    @Override
    public List<CarNumInOutTimeVo> queryInOutTime(String openId) throws Exception
    {
        LOGGER.info("进入查询车辆进出厂时间方法 openId={}",openId);
//        List<CarNumOpenId> carNumOpenIdList = carNumOpenIdMapper.selectByOpenIdCarNum(openId,null);
//        if(carNumOpenIdList == null || carNumOpenIdList.size() == 0)
//        {
//            LOGGER.info("当前用户未绑定车牌 openId={}",openId);
//            return null;
//        }
        LocalDate localDate = LocalDate.now();
//        System.out.println(localDate.getDayOfMonth());
        localDate = localDate.minusDays(localDate.getDayOfMonth());
        String startTime = localDate.minusMonths(1) + " 00:00:00";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String endTime = dateTimeFormatter.format(LocalDateTime.now());
        LOGGER.info("车辆进出开始时间：startTime={} 结束时间：endTime={}",startTime,endTime);
        List<CarNumInOutTimeVo> carNumInOutTimeVos = carNumOpenIdMapper.selectCarNumInOutTime(openId,startTime,endTime);
        LOGGER.info("结束查询车辆进出厂时间方法 openId={}",openId);
        return carNumInOutTimeVos;
    }
}
