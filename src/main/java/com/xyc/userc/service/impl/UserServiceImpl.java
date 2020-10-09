package com.xyc.userc.service.impl;

import com.alibaba.fastjson.JSON;
import com.xyc.userc.dao.*;
import com.xyc.userc.entity.MobileOpenId;
import com.xyc.userc.entity.Role;
import com.xyc.userc.entity.User;
import com.xyc.userc.service.UserService;
import com.xyc.userc.util.BusinessException;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.RoleTypeEnum;
import com.xyc.userc.vo.CarNumInfoVo;
import com.xyc.userc.vo.EnabledCarInfoVo;
import com.xyc.userc.vo.UserInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 2020/6/15.
 */
@Service("userService")
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class UserServiceImpl implements UserService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MobileMapper mobileMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MobileOpenIdMapper mobileOpenIdMapper;

    @Autowired
    private CarNumOpenIdMapper carNumOpenIdMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public User getUser(User user) throws Exception
    {
        String openId = user.getOpenid();
        LOGGER.info("进入获取用户信息方法 openid：{}",openId);
        List<Role> roleList = roleMapper.selectByOpenId(openId);
        user.setRoles(roleList);
        EnabledCarInfoVo enabledCarInfoVo = carNumOpenIdMapper.selectEnabledCarInfo(openId);
        if(enabledCarInfoVo != null) {
            String carNum = enabledCarInfoVo.getCarNum();
            LOGGER.info("获取到用户已启用的车牌号 carNum={}", carNum);
            user.setCarNum(carNum);
        }
        LOGGER.info("结束获取用户信息方法 openid：{}",openId);
        return user;
    }

    @Override
    public User bindMobileToOpenId(String mobile, String mesCode, String openId) throws Exception
    {
        LOGGER.info("进入绑定手机号方法 mobile={} mesCode={} openid={}",mobile,mesCode,openId);
//        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        MobileOpenId mobileOpenId = new MobileOpenId(null,mobile,openId,openId,new Date());
        LOGGER.info("开始校验短信验证码是否正确 mobile={} mesCode={}",mobile,mesCode);
        String storedMesCode = mobileMapper.selectMesCodeByMobile(mobile);
        if(storedMesCode == null)
        {
            LOGGER.info("短信验证码已失效 mobile={}",mobile);
            throw new BusinessException(JsonResultEnum.USER_MESCODE_EXPIRED);
        }
        else if(!storedMesCode.equals(mesCode))
        {
            LOGGER.info("短信验证码不正确 mobile={} mesCode={}",mobile,mesCode);
            throw new BusinessException(JsonResultEnum.USER_MESCODE_ERROR);
        }
        else
        {
            LOGGER.info("短信验证码输入正确 mobile={} mesCode={}", mobile,mesCode);
            LOGGER.info("开始设置验证码状态失效 mobile={} mesCode={}", mobile,mesCode);
            int status = 0;
            Date gmtModified = new Date();
            mobileMapper.updateMesCodeStatus(mobile,status,gmtModified);
            LOGGER.info("成功设置验证码失效状态 mobile={} mesCode={}", mobile,mesCode);

            int insertCnt = mobileOpenIdMapper.insertMobileOpenId(mobileOpenId);
            int id = mobileOpenId.getId();
            List<Map> maps = userMapper.selectUserRoleByOpenId(mobile);
            String roleCode = null;
            if(maps == null || maps.size() == 0)
            {
                roleCode = RoleTypeEnum.ROLE_SJ_0.getRoleCode();
            }
            else if(maps.size() == 1)
            {
                String item = maps.get(0).get("ITEM").toString();
                switch(item)
                {
                    case "XC":
                        roleCode = RoleTypeEnum.ROLE_JLY_XC.getRoleCode();
                        break;
                    case "BC":
                        roleCode = RoleTypeEnum.ROLE_JLY_BC.getRoleCode();
                        break;
                    case "HP":
                        roleCode = RoleTypeEnum.ROLE_JLY_XC.getRoleCode();
                        break;
                    default:
                        roleCode = RoleTypeEnum.ROLE_JLY_XC.getRoleCode();
                }
            }
            else
            {
                roleCode = RoleTypeEnum.ROLE_ADMIN.getRoleCode();
            }
            Role role = roleMapper.selectByRoleCode(roleCode);
            Date date = new Date();
            //插入用户与角色关联表
            roleMapper.insertUserRole(id,role.getId(),date,date);

            LOGGER.info("开始将用户绑定信息存入redis openId={} mobile={}",openId,mobile);
            UserInfoVo userInfoVo = new UserInfoVo();
            userInfoVo.setOpenId(openId);
            userInfoVo.setMobilePhone(mobile);
            userInfoVo.setRoleCode(roleCode);
            userInfoVo.setCarNumList(new ArrayList<CarNumInfoVo>());
            Map stockCodeMap = userMapper.selectStockCodeInfo(openId);
            if(stockCodeMap != null)
            {
                userInfoVo.setLastStockCode(stockCodeMap.get("LASTSTOCKCODE").toString());
                userInfoVo.setOperatorTime((Date)stockCodeMap.get("JLYOPERATORTIME"));
            }
            redisTemplate.opsForValue().set(openId,JSON.toJSONString(userInfoVo));
            //构建user对象返回
            User user = new User();
            user.setOpenid(openId);
            user.setMobilePhone(mobile);
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            user.setRoles(roles);
            LOGGER.info("结束绑定手机号方法 mobile={} mesCode={} openid={}",mobile,mesCode,openId);
            return user;
        }

    }

    @Override
    public void storeUserInfoVo() throws Exception
    {
        LOGGER.info("进入存储用户信息至redis方法");
        List<UserInfoVo> userInfoVoList = userMapper.selectUserInfoVo();
        if(userInfoVoList == null || userInfoVoList.size() == 0)
        {
            return;
        }
        List<Map> list = carNumOpenIdMapper.selectCarNumInfo();
        if(list != null && list.size() > 0)
        {
            String curOpenId = null;
            int listIdx = 0;
            int listSize = list.size();
            Map curMap = null;
            for (UserInfoVo userInfoVo : userInfoVoList)
            {
                curOpenId = userInfoVo.getOpenId();
                List<CarNumInfoVo> carNumInfoVoList = new ArrayList<>();
                while (listIdx < listSize)
                {
                    curMap = list.get(listIdx);
                    if(((String)curMap.get("OPENID")).equals(curOpenId))
                    {
                        CarNumInfoVo carNumInfoVo = new CarNumInfoVo();
                        carNumInfoVo.setCarNum(curMap.get("CARNUMBER").toString());
                        carNumInfoVo.setIsEnable(Integer.valueOf(curMap.get("IS_ENABLED").toString()));
                        carNumInfoVoList.add(carNumInfoVo);
                        listIdx++;
                    }
                    else
                    {
                        break;
                    }
                }
                userInfoVo.setCarNumList(carNumInfoVoList);
            }
        }
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.string());
        for(UserInfoVo userInfoVo : userInfoVoList)
        {
            String json = JSON.toJSONString(userInfoVo);
            String openId = userInfoVo.getOpenId();
            redisTemplate.opsForValue().set(openId,json);
        }
        LOGGER.info("结束存储用户信息至redis方法");
    }
}
