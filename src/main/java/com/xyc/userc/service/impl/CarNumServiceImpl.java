package com.xyc.userc.service.impl;

import com.xyc.userc.dao.*;
import com.xyc.userc.entity.CarNumOpenId;
import com.xyc.userc.service.CarNumService;
import com.xyc.userc.util.BusinessException;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.RoleTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
        List<Integer> mobileOpenIdIdList = carNumOpenIdMapper.selectByOpenId(openId);
        int deleteCnt = carNumOpenIdMapper.deleteByCarNumOpenId(carNum,openId);
        if(deleteCnt > 0)
        {
            if(mobileOpenIdIdList == null || mobileOpenIdIdList.size() == 1)
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
            throw new BusinessException(JsonResultEnum.DELETE_CARNUM_NOT_BINDED);
        }

        LOGGER.info("结束删除车牌号方法carNum={} openId={}",carNum,openId);
    }

    @Override
    public void addCarNum(String carNum, String openId) throws Exception
    {
        LOGGER.info("进入新增车牌号方法carNum={} openId={}",carNum,openId);
        Date date = new Date();
        List<Integer> mobileOpenIdIdList = carNumOpenIdMapper.selectByOpenId(openId);
        CarNumOpenId carNumOpenId = new CarNumOpenId(openId,carNum,0,openId,openId,date,date);
        int insertCnt = carNumOpenIdMapper.insert(carNumOpenId);
        if(insertCnt > 0 && (mobileOpenIdIdList == null || mobileOpenIdIdList.size() == 0))
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
}