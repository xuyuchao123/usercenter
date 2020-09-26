package com.xyc.userc.service.impl;

import com.xyc.userc.dao.*;
import com.xyc.userc.entity.MobileOpenId;
import com.xyc.userc.entity.Role;
import com.xyc.userc.entity.User;
import com.xyc.userc.service.UserService;
import com.xyc.userc.util.BusinessException;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.vo.EnabledCarInfoVo;
import com.xyc.userc.vo.UserInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    public Role bindMobileToOpenId(String mobile, String mesCode, String openId) throws Exception
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

            int i = mobileOpenIdMapper.insertMobileOpenId(mobileOpenId);
            List<Map> maps = userMapper.selectUserRoleByOpenId(openId);
            Role role = null;
            if(maps != null && maps.size() > 0)
            {
                Map map = maps.get(0);
                int mobileOpenIdId = Integer.parseInt(map.get("MOBILEOPENIDID").toString());
                int roleId = Integer.parseInt(map.get("ROLEID").toString());
                Date date = new Date();
                roleMapper.insertUserRole(mobileOpenIdId,roleId,date,date);
                String roleName = (String)map.get("ROLENAME");
                String roleCode = (String)map.get("ROLECODE");
                Integer isDeleted = Integer.parseInt(map.get("ISDELETED").toString());
                Date gmtCreate = (Date)map.get("GMTCREATE");
                Date gmtModified_role = (Date)map.get("GMTMODIFIED");
                Integer parentRoleId = map.get("PARENTROLEID") != null ?
                        Integer.parseInt(map.get("PARENTROLEID").toString()) : null;
                role = new Role(roleId,roleName,roleCode,isDeleted,gmtCreate,gmtModified_role,parentRoleId);
            }
            LOGGER.info("结束绑定手机号方法 role：{}",role);
            return role;
        }

    }

    @Override
    public List<UserInfoVo> getUserInfoVo() throws Exception
    {
        LOGGER.info("进入获取用户信息存至redis方法");
        List<UserInfoVo> userInfoVoList = userMapper.selectUserInfoVo();
        LOGGER.info("结束获取用户信息存至redis方法");
        return null;
    }
}
