package com.xyc.userc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xyc.userc.dao.*;
import com.xyc.userc.entity.CarNumOpenId;
import com.xyc.userc.entity.Role;
import com.xyc.userc.entity.User;
import com.xyc.userc.service.CarNumService;
import com.xyc.userc.util.BusinessException;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.RoleTypeEnum;
import com.xyc.userc.vo.CarNumInOutTimeVo;
import com.xyc.userc.vo.CarNumInfoVo;
import com.xyc.userc.vo.GsCarInfoVo;
import com.xyc.userc.vo.MobileOpenIdRoleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


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
        List<Map> maps = carNumOpenIdMapper.confirmCarNumExist(openId,carNum);
        if(maps == null || maps.size() == 0)
        {
            LOGGER.info("当前用户未绑定该车牌 carNum={} openId={}",carNum,openId);
            throw new BusinessException(JsonResultEnum.CARNUM_NOT_BINDED);
        }
        Map map = maps.get(0);
        String roleCode = map.get("ROLE_CODE").toString();
        int mobileOpenIdId = Integer.valueOf(map.get("ID").toString());
        int deleteCnt = carNumOpenIdMapper.deleteByCarNumOpenId(carNum,openId);
        boolean needChgRole = false;
        if(deleteCnt > 0)
        {
            String jsonString = (String)redisTemplate.opsForValue().get(openId);
            List<CarNumOpenId> CarNumOpenIdList = carNumOpenIdMapper.selectByOpenIdCarNum(openId,null);
            if((CarNumOpenIdList == null || CarNumOpenIdList.size() == 0)
                    && RoleTypeEnum.ROLE_SJ_1.getRoleCode().equals(roleCode))
            {
                LOGGER.info("删除成功，当前用户角色为SJ1且没有已绑定的车牌号，准备修改其角色 carNum={} openId={}",carNum,openId);
                //获取角色"司机0"
                Role role = roleMapper.selectByRoleCode(RoleTypeEnum.ROLE_SJ_0.getRoleCode());
                if(role != null)
                {
                    //更新当前用户的角色为司机0
                    userMapper.updateRoleIdByMobileOpenId(role.getId(),mobileOpenIdId,new Date());
                    needChgRole = true;
                    LOGGER.info("角色修改成功carNum={} openId={}",carNum,openId);
                }
            }
            if(jsonString != null)
            {
                LOGGER.info("开始删除redis中用户的车牌号列表信息 openId={}",openId);
                JSONObject jsonObject = JSONObject.parseObject(jsonString);
                //删除redis中用户的车牌号列表
                List carNumList = (List)jsonObject.get("carNumList");
                if(carNumList != null && carNumList.size() > 0)
                {
                    for (Object obj : carNumList)
                    {
                        if(carNum.equals(((CarNumInfoVo)obj).getCarNum()))
                        {
                            carNumList.remove(obj);
                            break;
                        }
                    }
                }
                if(needChgRole)
                {
                    //更新redis中用户的角色
                    LOGGER.info("开始更新redis中用户的角色信息 openId={}",openId);
                    jsonObject.replace("roleCode",RoleTypeEnum.ROLE_SJ_0.getRoleCode());
                }
                redisTemplate.opsForValue().set(openId,JSON.toJSONString(jsonObject));
                LOGGER.info("结束更新redis中用户的车牌号列表及角色信息 openId={}",openId);
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
    public void addCarNum(String carNum, String openId, String engineNum, String identNum,String emissionStd) throws Exception
    {
        LOGGER.info("进入新增车牌号方法carNum={} openId={}",carNum,openId);
        LOGGER.info("开始检查车牌号是否已被绑定carNum={} openId={}",carNum,openId);
        int cnt = carNumOpenIdMapper.selectCntByCarNumOpenId(carNum,null);
        Date date = new Date();
        CarNumOpenId carNumOpenId = new CarNumOpenId(null,openId,carNum,engineNum,identNum,emissionStd,0,0,openId,openId,date,date);
        if(cnt > 0)
        {
            LOGGER.info("该车牌号已被绑定，不能重复绑定 carNum={} openId={}",carNum,openId);
            throw new BusinessException(JsonResultEnum.CARNUM_BINDED);
        }
        int insertCnt = carNumOpenIdMapper.insert(carNumOpenId);
//        List<Integer> mobileOpenIdIdList = carNumOpenIdMapper.selectMobileOpenIdIdByOpenId(openId);
//        if(insertCnt > 0 && (mobileOpenIdIdList != null && mobileOpenIdIdList.size() == 1))
//        {
//            LOGGER.info("新增车牌成功，当前用户仅绑定一个车牌号，准备修改其角色carNum={} openId={}",carNum,openId);
//            //获取角色"司机1"的id
//            int roleId = roleMapper.selectIdByRoleCode(RoleTypeEnum.ROLE_SJ_1.getRoleCode());
//            //更新当前用户的角色为司机1
//            userMapper.updateRoleIdByMobileOpenId(roleId,mobileOpenIdIdList.get(0));
//            LOGGER.info("角色修改成功carNum={} openId={}",carNum,openId);
//        }
        if(insertCnt > 0)
        {
            LOGGER.info("新增车牌成功 carNum={} openId={}",carNum,openId);
            String jsonString = (String)redisTemplate.opsForValue().get(openId);
            if(jsonString != null)
            {
                LOGGER.info("开始添加redis中用户的车牌号列表信息 carNum={} openId={}",carNum,openId);
                JSONObject jsonObject = JSONObject.parseObject(jsonString);
                //新增redis中用户的车牌号列表
                List carNumList = (List)jsonObject.get("carNumList");
                if(carNumList != null)
                {
                    CarNumInfoVo carNumInfoVo = new CarNumInfoVo();
                    carNumInfoVo.setCarNum(carNum);
                    carNumInfoVo.setIsEnable(0);
                    carNumList.add(carNumInfoVo);
                }
                redisTemplate.opsForValue().set(openId,JSON.toJSONString(jsonObject));
                LOGGER.info("结束添加redis中用户的车牌号列表信息 carNum={} openId={}",carNum,openId);
            }
        }
        else
        {
            LOGGER.info("该车牌号已被绑定，不能重复绑定 carNum={} openId={}",carNum,openId);
            throw new BusinessException(JsonResultEnum.CARNUM_BINDED);
        }
        LOGGER.info("结束新增车牌号方法 carNum={} openId={}",carNum,openId);
    }

    @Override
    public void modifyCarNumByOpenId(String oldCarNum, String newCarNum, String engineNum,
                                     String identNum, String emissionStd, String openId) throws Exception
    {
        LOGGER.info("进入修改车牌号方法 oldCarNum={} newCarNum={} engineNum={} identNum={} emissionStd={} openId={}",
                oldCarNum,newCarNum,engineNum,identNum,emissionStd,openId);
        int selectCnt = carNumOpenIdMapper.selectCntByCarNumOpenId(oldCarNum,openId);
        if(selectCnt == 1)
        {
            carNumOpenIdMapper.updateCarNum(oldCarNum,newCarNum,engineNum,identNum,emissionStd,openId,new Date());
            LOGGER.info("成功修改车牌号 openId={}",openId);
            String jsonString = (String)redisTemplate.opsForValue().get(openId);
            if(jsonString != null)
            {
                LOGGER.info("开始修改redis中用户的车牌号列表信息 oldCarNum={} newCarNum={} openId={}", oldCarNum,newCarNum,openId);
                JSONObject jsonObject = JSONObject.parseObject(jsonString);
                //修改redis中用户的车牌号列表
                List carNumList = (List)jsonObject.get("carNumList");
                if(carNumList != null && carNumList.size() > 0)
                {
                    for(Object obj : carNumList)
                    {
                        if(oldCarNum.equals(((JSONObject)obj).get("carNum").toString()))
                        {
                            ((JSONObject)obj).replace("carNum",newCarNum);
                            redisTemplate.opsForValue().set(openId,JSON.toJSONString(jsonObject));
                            break;
                        }
                    }
                }
                LOGGER.info("结束修改redis中用户的车牌号列表信息 oldCarNum={} newCarNum={} openId={}", oldCarNum,newCarNum,openId);
            }
        }
        else
        {
            LOGGER.info("该用户未绑定该车牌，修改失败 openId={}",openId);
            throw new BusinessException(JsonResultEnum.CARNUM_NOT_BINDED);
        }
        LOGGER.info("结束修改车牌号方法 oldCarNum={} newCarNum={} engineNum={} identNum={} emissionStd={} openId={}",
                oldCarNum,newCarNum,engineNum,identNum,emissionStd,openId);
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
        carNumOpenIdMapper.updateCarNumEnable(0,new Date(),null,openId);
        carNumOpenIdMapper.updateCarNumEnable(1,new Date(),carNumOpenId.getCarNum(),openId);
        LOGGER.info("成功启用车牌号 carNum={} openId={}", carNum,openId);
        boolean needChgRole = false;
        //查找当前用户的角色
        List<MobileOpenIdRoleVo> mobileOpenIdRoleVoList = roleMapper.selectMobileOpenIdRoleVo(openId);
        if(mobileOpenIdRoleVoList != null && mobileOpenIdRoleVoList.size() > 0
                && RoleTypeEnum.ROLE_SJ_0.getRoleCode().equals(mobileOpenIdRoleVoList.get(0).getRoleCode()))
        {
            LOGGER.info("当前用户角色为SJ0，准备修改其角色为SJ1 carNum={} openId={}",carNum,openId);

            //获取角色"司机1"
            Role role = roleMapper.selectByRoleCode(RoleTypeEnum.ROLE_SJ_1.getRoleCode());
            if(role != null)
            {
                //更新当前用户的角色为司机1
                userMapper.updateRoleIdByMobileOpenId(role.getId(),mobileOpenIdRoleVoList.get(0).getMobileOpenIdId(),new Date());
                needChgRole = true;
                LOGGER.info("角色修改成功 carNum={} openId={}",carNum,openId);
            }
        }
        String jsonString = (String)redisTemplate.opsForValue().get(openId);
        if(jsonString != null)
        {
            LOGGER.info("开始修改redis中用户车牌号启用信息 carNum={} openId={}",carNum,openId);
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            List carNumList = (List)jsonObject.get("carNumList");
            if(carNumList != null && carNumList.size() > 0)
            {
                for(Object obj : carNumList)
                {
                    if(carNum.equals(((JSONObject)obj).get("carNum")))
                    {
                        ((JSONObject)obj).replace("isEnable",1);
                    }
                    else
                    {
                        ((JSONObject)obj).replace("isEnable",0);
                    }
                }
            }
            if(needChgRole)
            {
                //更新redis中用户的角色
                LOGGER.info("开始更新redis中用户的角色信息 carNum={} openId={}",carNum,openId);
                jsonObject.replace("roleCode",RoleTypeEnum.ROLE_SJ_1.getRoleCode());
            }
            redisTemplate.opsForValue().set(openId,JSON.toJSONString(jsonObject));
            LOGGER.info("结束修改redis中用户车牌号启用及角色信息 carNum={} openId={}",carNum,openId);
        }
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
        String startTime = localDate.minusMonths(1) + " 00:00:00";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
}
