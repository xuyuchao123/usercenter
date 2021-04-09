package com.xyc.userc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xyc.userc.dao.*;
import com.xyc.userc.entity.CarNumFrozen;
import com.xyc.userc.entity.CarNumOpenId;
import com.xyc.userc.entity.Role;
import com.xyc.userc.entity.User;
import com.xyc.userc.service.CarNumService;
import com.xyc.userc.service.RedisService;
import com.xyc.userc.util.*;
import com.xyc.userc.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    @Autowired
    private RedisService redisService;


    @Override
    public List<CarNumOpenIdVo> getCarNum(String openId) throws Exception
    {
        LOGGER.info("进入查询车牌号方法 openId:{}",openId);
        List<CarNumOpenIdVo> carNumOpenIdVos = carNumOpenIdMapper.selectByOpenId(openId);
        LOGGER.info("结束查询车牌号方法 openId:{}",openId);
        return carNumOpenIdVos;
    }

    @Override
    public void removeCarNum(String carNum, String openId) throws Exception
    {
        LOGGER.info("进入删除车牌号方法carNum={} openId={}",carNum,openId);
        List<Map> maps = carNumOpenIdMapper.confirmCarNumExist(openId,carNum);
        if(maps == null || maps.size() == 0)
        {
            LOGGER.info("当前用户未绑定该车牌 carNum={} openId={}",carNum,openId);
            throw new BusinessException(JsonResultEnum.CARNUM_NOT_BINDED);
        }
//        Map map = maps.get(0);
//        String roleCode = map.get("ROLE_CODE").toString();
        List<String> roleCodeList = new ArrayList<>();
        int roleHlddId = -1;
        for(Map map : maps)
        {
            String tmpRoleCode = map.get("ROLE_CODE").toString();
            roleCodeList.add(tmpRoleCode);
            //获取角色 HLDD 对应的roleId
            if(RoleTypeEnum.ROLE_HLDD.getRoleCode().equals(tmpRoleCode))
            {
                roleHlddId = Integer.valueOf(map.get("ROLEID").toString());
            }
        }
        int mobileOpenIdId = Integer.valueOf(maps.get(0).get("ID").toString());
        int deleteCnt = carNumOpenIdMapper.deleteByCarNumOpenId(carNum,openId);
        if(deleteCnt > 0)
        {
            //查找该openid下已启用的车牌号数量
            int  enabledCarNumCnt = carNumOpenIdMapper.confirmEnabledCarNumExist(openId,null);
            //若删除车牌后该openid下没有已启用的车牌并且角色包含SJ1或HLDD,则修改其角色
            if((enabledCarNumCnt == 0) && (roleCodeList.contains(RoleTypeEnum.ROLE_SJ_1.getRoleCode()) || roleCodeList.contains(RoleTypeEnum.ROLE_HLDD.getRoleCode())) )
            {
                LOGGER.info("删除成功，当前用户角色包含SJ1或HLDD 且没有已绑定的车牌号，准备修改其角色 carNum={} openId={}",carNum,openId);
                //获取角色"司机0"
                Role role = roleMapper.selectByRoleCode(RoleTypeEnum.ROLE_SJ_0.getRoleCode());
                if(role != null)
                {
                    //更新当前用户的角色为司机0
                    if(roleCodeList.size() == 1)
                    {
                        userMapper.updateRoleIdByMobileOpenId(role.getId(),mobileOpenIdId,new Date());
                        LOGGER.info("角色修改成功carNum={} openId={}",carNum,openId);
                    }
                    else
                    {
                        //多个角色的情况下只更新其中的一个
                        if(roleHlddId != -1)
                        {
                            userMapper.updateRoleIdByMobileOpenIdRoleId(role.getId(),mobileOpenIdId,roleHlddId,new Date());
                        }
                    }
                }
            }
            LOGGER.info("开始更新redis中用户车牌号信息 openId={}",openId);
            redisService.updateRedis(openId);
            LOGGER.info("结束更新redis中用户车牌号信息 openId={}",openId);
        }
        else
        {
            LOGGER.info("当前用户没有绑定该车牌号,删除失败！carNum={} openId={}",carNum,openId);
            throw new BusinessException(JsonResultEnum.CARNUM_NOT_BINDED);
        }

        LOGGER.info("结束删除车牌号方法carNum={} openId={}",carNum,openId);
    }

    @Override
    public void addCarNum(String carNum,String openId,String engineNum,String identNum,String emissionStd,
                          String fleetName,Date regDate, String department, String drivingLicense) throws Exception
    {
        LOGGER.info("进入新增车牌号方法carNum={} openId={}",carNum,openId);
        LOGGER.info("开始检查车牌号是否已被绑定carNum={} openId={}",carNum,openId);
        int cnt = carNumOpenIdMapper.selectCntByCarNumOpenId(carNum,openId);
        Date date = new Date();
        CarNumOpenId carNumOpenId = new CarNumOpenId(null,openId,carNum,engineNum,identNum,emissionStd,fleetName,regDate,department,0,0,openId,openId,date,date);
        if(cnt > 0)
        {
            LOGGER.info("该车牌号已被绑定，不能重复绑定 carNum={} openId={}",carNum,openId);
            throw new BusinessException(JsonResultEnum.CARNUM_BINDED);
        }
        if(drivingLicense != null && !drivingLicense.equals(""))
        {
            LOGGER.info("开始检查车牌号是否已上传过行驶证 carNum={} openId={}",carNum,openId);
            int licenseCnt = carNumOpenIdMapper.selectDrivingLicense(carNum);
            if(licenseCnt > 0)
            {
                LOGGER.info("该车牌号已上传过行驶证，不能重复上传 carNum={} openId={}",carNum,openId);
                throw new BusinessException(JsonResultEnum.DRIVINGLICENSE_EXIST);
            }
        }
        int insertCnt = carNumOpenIdMapper.insert(carNumOpenId);
        if(insertCnt > 0)
        {
            LOGGER.info("新增车牌成功 carNum={} openId={}",carNum,openId);
            if(drivingLicense != null && !drivingLicense.equals(""))
            {
                LOGGER.info("开始上传行驶证 carNum={} openId={} drivingLicense{}",carNum,openId,drivingLicense);
                carNumOpenIdMapper.insertDrivingLicense(carNum,openId,drivingLicense);
            }
            LOGGER.info("开始更新redis中用户车牌号信息 openId={}",openId);
            redisService.updateRedis(openId);
            LOGGER.info("结束更新redis中用户车牌号信息 openId={}",openId);
        }
        else
        {
            LOGGER.info("该车牌号已被绑定，不能重复绑定 carNum={} openId={}",carNum,openId);
            throw new BusinessException(JsonResultEnum.CARNUM_BINDED);
        }
        LOGGER.info("结束新增车牌号方法 carNum={} openId={}",carNum,openId);
    }

    @Override
    public void modifyCarNumByOpenId(String oldCarNum, String newCarNum, String engineNum, String identNum,
                                     String emissionStd, String fleetName, Date regDate, String department, String openId) throws Exception
    {
        LOGGER.info("进入修改车牌号方法 oldCarNum={} newCarNum={} engineNum={} identNum={} emissionStd={} fleetName={} regDate={} department={} openId={}",
                oldCarNum,newCarNum,engineNum,identNum,emissionStd,fleetName,regDate,department,openId);
        int selectCnt = carNumOpenIdMapper.selectCntByCarNumOpenId(oldCarNum,openId);
        if(selectCnt == 1)
        {
            carNumOpenIdMapper.updateCarNum(oldCarNum,newCarNum,engineNum,identNum,emissionStd,fleetName,regDate,department,openId,new Date());
            LOGGER.info("成功修改车牌号 openId={}",openId);

            LOGGER.info("开始更新redis中用户车牌号信息openId={}",openId);
            redisService.updateRedis(openId);
            LOGGER.info("结束更新redis中用户车牌号信息 openId={}",openId);
        }
        else
        {
            LOGGER.info("该用户未绑定该车牌，修改失败 openId={}",openId);
            throw new BusinessException(JsonResultEnum.CARNUM_NOT_BINDED);
        }
        LOGGER.info("结束修改车牌号方法 oldCarNum={} newCarNum={} engineNum={} identNum={} emissionStd={} fleetName={} regDate={} department={} openId={}",
                oldCarNum,newCarNum,engineNum,identNum,emissionStd,fleetName,regDate,department,openId);
    }

    @Override
    public void enableCarNum(String carNum, String openId) throws Exception
    {
        LOGGER.info("进入启用车牌号方法 CarNum={} openId={}",carNum,openId);
        List<CarNumOpenId> carNumOpenIdList = carNumOpenIdMapper.selectByOpenIdCarNum(openId,carNum);
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
        carNumOpenIdMapper.updateCarNumEnable(0,new Date(),null,openId);
        carNumOpenIdMapper.updateCarNumEnable(1,new Date(),carNumOpenId.getCarNum(),openId);
        LOGGER.info("成功启用车牌号 carNum={} openId={}", carNum,openId);
        //查找当前用户的角色
        List<MobileOpenIdRoleVo> mobileOpenIdRoleVoList = roleMapper.selectMobileOpenIdRoleVo(openId);
        if(mobileOpenIdRoleVoList != null && mobileOpenIdRoleVoList.size() > 0)
        {
            if(mobileOpenIdRoleVoList.size() == 1)
            {
                if(RoleTypeEnum.ROLE_SJ_0.getRoleCode().equals(mobileOpenIdRoleVoList.get(0).getRoleCode()))
                {
                    LOGGER.info("当前用户角色为SJ0，判断其原始角色是否为 HLDD, carNum={} openId={}",carNum,openId);
                    List<Map> maps = userMapper.selectUserRoleByOpenIdRoleCode(openId,RoleTypeEnum.ROLE_HLDD.getRoleCode());
                    if(maps != null && maps.size() > 0)
                    {
                        LOGGER.info("原始角色为 HLDD, carNum={} openId={}",carNum,openId);
                        //获取角色"HLDD"
                        Role role = roleMapper.selectByRoleCode(RoleTypeEnum.ROLE_HLDD.getRoleCode());
                        if(role != null)
                        {
                            //更新当前用户的角色为HLDD
                            userMapper.updateRoleIdByMobileOpenId(role.getId(),mobileOpenIdRoleVoList.get(0).getMobileOpenIdId(),new Date());
                            LOGGER.info("角色修改成功 carNum={} openId={}",carNum,openId);
                        }
                    }
                    else
                    {
                        LOGGER.info("原始角色不为 HLDD, carNum={} openId={}",carNum,openId);
                        //获取角色"司机1"
                        Role role = roleMapper.selectByRoleCode(RoleTypeEnum.ROLE_SJ_1.getRoleCode());
                        if(role != null)
                        {
                            //更新当前用户的角色为司机1
                            userMapper.updateRoleIdByMobileOpenId(role.getId(),mobileOpenIdRoleVoList.get(0).getMobileOpenIdId(),new Date());
                            LOGGER.info("角色修改成功 carNum={} openId={}",carNum,openId);
                        }
                    }

                }
            }
            else
            {
                for(MobileOpenIdRoleVo mobileOpenIdRoleVo : mobileOpenIdRoleVoList)
                {
                    if(RoleTypeEnum.ROLE_SJ_0.getRoleCode().equals(mobileOpenIdRoleVo.getRoleCode()))
                    {
                        int sjId = mobileOpenIdRoleVo.getMobileOpenIdId();
                        int oriRoleId = mobileOpenIdRoleVo.getRoleId();
                        LOGGER.info("该用户对应多个角色，其中存在 SJ0 角色，准备修改其为 HLDD, sjId={} oriRoleId={} carNum={} openId={}", sjId,oriRoleId,carNum,openId);
                        //获取角色"HLDD"
                        Role role = roleMapper.selectByRoleCode(RoleTypeEnum.ROLE_HLDD.getRoleCode());
                        if(role != null)
                        {
                            //修改当前用户的 SJ0 角色为 HLDD
                            userMapper.updateRoleIdByMobileOpenIdRoleId(role.getId(),sjId,oriRoleId,new Date());
                            LOGGER.info("角色修改成功 carNum={} openId={}",carNum,openId);
                        }
                        break;
                    }
                }
            }
        }

        LOGGER.info("开始更新redis中用户车牌号信息openId={}",openId);
        redisService.updateRedis(openId);
        LOGGER.info("结束更新redis中用户车牌号信息 openId={}",openId);

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
        int day = localDate.getDayOfMonth();
        if(day > 1)
        {
            localDate = localDate.minusDays(day-1);
        }
        //开始时间为上个月1号0点
        String startTime = localDate.minusMonths(1) + " 00:00:00";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //结束时间为当前时间
        String endTime = dateTimeFormatter.format(LocalDateTime.now());
        LOGGER.info("车辆进出开始时间：startTime={} 结束时间：endTime={}",startTime,endTime);
        List<CarNumInOutTimeVo> carNumInOutTimeVos = carNumOpenIdMapper.selectCarNumInOutTime(openId,startTime,endTime);
        LOGGER.info("结束查询车辆进出厂时间方法 openId={}",openId);
        return carNumInOutTimeVos;
    }

    @Override
    public List<GsCarInfoVo> queryGsCarInfo(String carNum) throws Exception
    {
        LOGGER.info("进入查询国三车辆识别号及发动机号方法 carNum={}",carNum);
        List<GsCarInfoVo> gsCarInfoVos = carNumOpenIdMapper.selectGsCarInfoByCarNum(carNum);
        LOGGER.info("结束查询国三车辆识别号及发动机号方法 carNum={}",carNum);
        return gsCarInfoVos;
    }

    @Override
    public List queryEnvInfo(String carNum, String startDate, String page, String size) throws Exception
    {
        LOGGER.info("进入查询车辆环保信息方法carNum={},startDate={},page={},size={}",carNum,startDate,page,size);
        if(page == null)
        {
            throw new BusinessException(JsonResultEnum.PAGE_NOT_EXIST);
        }
        if(size == null)
        {
            throw new BusinessException(JsonResultEnum.SIZE_NOT_EXIST);
        }
        if(startDate == null)
        {
            //查询时间为当前日期
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startDate = dateTimeFormatter.format(LocalDate.now());
        }
        //若carNum为空字符串，则按照carNum为空处理
        if(carNum != null && carNum.equals(""))
        {
            carNum = null;
        }

        int pageInt = Integer.valueOf(page);
        int sizeInt = Integer.valueOf(size);

        //查询总记录数
        int cnt = carNumOpenIdMapper.selectEnvInfoCnt(carNum,startDate);
        int pageCnt = cnt / sizeInt + 1;
        if(pageInt > pageCnt)
        {
            pageInt = pageCnt;
        }
//        int cnt = 0;
        int start = (pageInt - 1) * sizeInt + 1;
        int end = start + sizeInt - 1;
        LOGGER.info("查询车辆环保信息 startDate={},start={},end={},cnt={}",startDate,start,end,cnt);
        List<EnvInfoVo> envInfoVoList = carNumOpenIdMapper.selectEnvInfo(carNum,startDate,start,end);
        List resList = new ArrayList();
        resList.add(cnt);
        resList.add(pageInt);
        resList.add(sizeInt);
        resList.add(envInfoVoList);
        LOGGER.info("结束查询车辆环保信息方法carNum={},startDate={},page={},size={}",carNum,startDate,page,size);
        return resList;
    }

    @Override
    public boolean queryDrivinglicense(String carNum) throws Exception
    {
        LOGGER.info("进入校验行驶证信息方法carNum={}",carNum);
        int  licenseCnt = carNumOpenIdMapper.selectDrivingLicense(carNum);
        LOGGER.info("结束校验行驶证信息方法carNum={} licenseCnt={}",carNum,licenseCnt);
        return licenseCnt > 0 ? true : false;
    }

    @Override
    public void refreshCarNumFrozen() throws Exception
    {
        LOGGER.info("进入更新车牌号冻结情况方法");
        List<CarNumFrozen> carNumFrozens = carNumOpenIdMapper.selectCarNumFrozen();
        Map<String,CarNumFrozen> frozenMap = new HashMap<>();
        for(CarNumFrozen carNumFrozen : carNumFrozens)
        {
            frozenMap.put(carNumFrozen.getCarNum(),carNumFrozen);
        }
        //从安全系统获取车辆A类违章信息
//        List<Map> violationInfoList = carNumOpenIdMapper.selectCarNumAViolationInfo();

        //测试数据
        List<Map> violationInfoList = new ArrayList<>();
        Map<String,Object> tstmap = new HashMap();
        tstmap.put("carNum","aaaa");
        tstmap.put("violationTimes",2);
        violationInfoList.add(tstmap);

        Map<String,Object> tstmap2 = new HashMap();
        tstmap2.put("carNum","bbbb");
        tstmap2.put("violationTimes",1);
        violationInfoList.add(tstmap2);

        Map<String,Object> tstmap3 = new HashMap();
        tstmap3.put("carNum","cccc");
        tstmap3.put("violationTimes",3);
        violationInfoList.add(tstmap3);

        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = DateUtils.localDateTime2Date(localDateTime);
        //首次违章的车牌号列表，
        List<CarNumFrozen> insertList = new ArrayList<>();
        //当前未冻结，需要再次进行冻结的车牌号列表
        List<CarNumFrozen> recoverList = new ArrayList<>();
        //当前已冻结，违章次数增加，需要修改冻结期限的车牌号列表
        List<CarNumFrozen> delayList = new ArrayList<>();
        //冻结期限到期，需要解冻的车牌号列表
        List<CarNumFrozen> unfreezeList = new ArrayList<>();
        if(violationInfoList != null && violationInfoList.size()>0)
        {
            for(Map map : violationInfoList)
            {
                String loopCarNum = map.get("carNum").toString();
                int loopTimes = (Integer)map.get("violationTimes");
                CarNumFrozen tmpCarNumFrozen = frozenMap.get(loopCarNum);
                if(tmpCarNumFrozen == null)     //没有冻结记录
                {
                    Date expireDate = DateUtils.localDateTime2Date(localDateTime.plusDays(loopTimes * UsercConstant.FROZEN_PERIOD));
                    CarNumFrozen instCarNumFrozen = new CarNumFrozen(null,loopCarNum,1,date,expireDate,date,date,loopTimes);
                    insertList.add(instCarNumFrozen);
                }
                else    //有冻结记录
                {
                    int preTimes = tmpCarNumFrozen.getViolationTimes().intValue();
                    if(tmpCarNumFrozen.getFrozenStatus().intValue() == 0)       //在解冻状态
                    {
                        if(preTimes < loopTimes)  //有新的违章记录
                        {
                            Date expireDate = DateUtils.localDateTime2Date(localDateTime.plusDays((loopTimes- preTimes)*UsercConstant.FROZEN_PERIOD));
                            tmpCarNumFrozen.setFrozenStatus(1);
                            tmpCarNumFrozen.setStartDate(date);
                            tmpCarNumFrozen.setExpireDate(expireDate);
                            tmpCarNumFrozen.setGmtModified(date);
                            tmpCarNumFrozen.setViolationTimes(loopTimes);
                            recoverList.add(tmpCarNumFrozen);
                        }
                        frozenMap.remove(loopCarNum);
                    }
                    else    //在冻结状态
                    {
                        if(preTimes < loopTimes)  //有新的违章记录
                        {
                            Date expireDate = DateUtils.localDateTime2Date(DateUtils.date2LocalDateTime(tmpCarNumFrozen.getExpireDate())
                                    .plusDays((loopTimes- preTimes)*UsercConstant.FROZEN_PERIOD));
                            tmpCarNumFrozen.setExpireDate(expireDate);
                            tmpCarNumFrozen.setGmtModified(date);
                            tmpCarNumFrozen.setViolationTimes(loopTimes);
                            delayList.add(tmpCarNumFrozen);
                            frozenMap.remove(loopCarNum);
                        }
                    }
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
                unfreezeList.add(tmpCarNumFrozen2);
            }
        }
        recoverList.addAll(delayList);
        recoverList.addAll(unfreezeList);
        if(recoverList.size() > 0)
        {
            carNumOpenIdMapper.updateAllCarNumFrozen(recoverList);
        }
        if(insertList.size() > 0)
        {
            carNumOpenIdMapper.insertAllCarNumFrozen(insertList);
        }
        LOGGER.info("结束更新车牌号冻结情况方法");

    }
}
