package com.xyc.userc.service.impl;

import com.alibaba.fastjson.JSON;
import com.xyc.userc.dao.*;
import com.xyc.userc.entity.MobileOpenId;
import com.xyc.userc.entity.PcUser;
import com.xyc.userc.entity.Role;
import com.xyc.userc.entity.User;
import com.xyc.userc.security.MesCodeErrorException;
import com.xyc.userc.security.MesCodeExpiredException;
import com.xyc.userc.security.MobileNotFoundException;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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
    private PcUserMapper pcUserMapper;

    @Autowired
    private MobileMapper mobileMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MobileOpenIdMapper mobileOpenIdMapper;

    @Autowired
    private CarNumOpenIdMapper carNumOpenIdMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
    public User bindMobileToOpenId(String mobile, String nickName,String mesCode, String openId) throws Exception
    {
        LOGGER.info("进入绑定手机号方法 mobile={} nickName={} mesCode={} openid={}",mobile,nickName,mesCode,openId);
//        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        MobileOpenId mobileOpenId = new MobileOpenId(null,mobile,nickName,openId,openId,new Date());
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

            LOGGER.info("开始查询当前openId是否已绑定其它手机号 mobile={} openId={}", mobile,openId);
            Map map = mobileOpenIdMapper.selectByMobileOpenIdRole(null,openId);
            String roleCode = null;
            String gh = null;
            Role role = null;
            if(map != null)
            {
                String mobile_ori = map.get("MOBILE").toString();
                LOGGER.info("当前openId已绑定手机号 mobile_ori={} openId={}", mobile_ori,openId);
                if(mobile_ori.equals(mobile))
                {
                    LOGGER.error("当前openId已绑定该手机号，不能重复绑定 mobile={} openId={}", mobile,openId);
                    throw new BusinessException(JsonResultEnum.MOBILE_BINDED);
                }
                else
                {
                    LOGGER.info("开始更新当前openId绑定的手机号 mobile={} openId={}", mobile,openId);
                    mobileOpenIdMapper.updateMobile(mobile,openId);
                    String[] resArray = queryRoleCodeByMobile(mobile);
                    roleCode = resArray[0];
                    gh = resArray[1];
                    LOGGER.info("当前手机号：{},对应的角色编码：{}, 工号：{}",mobile,roleCode,gh);
                    role = roleMapper.selectByRoleCode(roleCode);
                    //更新后的手机号对应的角色与原手机号对应的角色不同则需要修改角色
                    if(!role.getId().equals(map.get("ROLE_ID").toString()))
                    {
                        Date date = new Date();
                        //更新用户与角色关联表
                        roleMapper.updateUserRole(Integer.valueOf(map.get("ID").toString()),role.getId(),date);
                    }
                }
            }
            else
            {
                LOGGER.info("当前openId未绑定手机号openId={}", openId);
                int insertCnt = mobileOpenIdMapper.insertMobileOpenId(mobileOpenId);
                int id = mobileOpenId.getId();
                String[] resArray = queryRoleCodeByMobile(mobile);
                roleCode = resArray[0];
                gh = resArray[1];
                LOGGER.info("当前手机号：{},对应的角色编码：{}, 工号：{}",mobile,roleCode,gh);
                role = roleMapper.selectByRoleCode(roleCode);
                Date date = new Date();
                //插入用户与角色关联表
                roleMapper.insertUserRole(id, role.getId(), date, date);
            }

            LOGGER.info("开始将用户绑定信息存入redis openId={} mobile={}", openId, mobile);
            UserInfoVo userInfoVo = new UserInfoVo();
            userInfoVo.setOpenId(openId);
            userInfoVo.setMobilePhone(mobile);
            userInfoVo.setRoleCode(roleCode);
            if (roleCode.equals(RoleTypeEnum.ROLE_SJ_0.getRoleCode()))
            {
                userInfoVo.setCarNumList(new ArrayList<CarNumInfoVo>());
            }
            if (roleCode.equals(RoleTypeEnum.ROLE_JLY_BC.getRoleCode()) || roleCode.equals(RoleTypeEnum.ROLE_JLY_XC.getRoleCode()) ||
                    roleCode.equals(RoleTypeEnum.ROLE_JLY_KHB.getRoleCode()) || roleCode.equals(RoleTypeEnum.ROLE_JLY_OTHER.getRoleCode()))
            {
                Map stockCodeMap = userMapper.selectStockCodeInfo(openId);
                if (stockCodeMap != null)
                {
                    userInfoVo.setLastStockCode(stockCodeMap.get("LASTSTOCKCODE").toString());
                    userInfoVo.setOperatorTime((Date) stockCodeMap.get("JLYOPERATORTIME"));
                }
            }
//            角色为开单员则获取其工号
//            if(roleCode.equals(RoleTypeEnum.ROLE_KDY.getRoleCode()))
//            {
//                userInfoVo.setGh(gh);
//            }
            userInfoVo.setGh(gh);
            redisTemplate.opsForValue().set(openId, JSON.toJSONString(userInfoVo));

            //构建user对象返回
            User user = new User();
            user.setOpenid(openId);
            user.setMobilePhone(mobile);
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            user.setRoles(roles);
            LOGGER.info("结束绑定手机号方法 mobile={} nickName={} mesCode={} openid={}",mobile,nickName,mesCode,openId);
            return user;
        }

    }

    @Override
    public void storeUserInfoVo() throws Exception
    {
        LOGGER.info("进入存储用户信息至redis方法");
        List<UserInfoVo> userInfoVoList = userMapper.selectUserInfoVo(null);
        if(userInfoVoList == null || userInfoVoList.size() == 0)
        {
            return;
        }
        List<Map> list = carNumOpenIdMapper.selectCarNumInfo(null);
        List KdyMobileList = null;
        if(list != null && list.size() > 0)
        {
            String curOpenId = null;
            int listIdx = 0;
            int listSize = list.size();
            Map curMap = null;
            KdyMobileList = new ArrayList<String>();
            for (UserInfoVo userInfoVo : userInfoVoList)
            {
                //若是开单员角色则收集其手机号到 KdyMobileList 中，方便统一查询对应的工号
                if(RoleTypeEnum.ROLE_KDY.getRoleCode().equals(userInfoVo.getRoleCode()))
                {
                    KdyMobileList.add(userInfoVo.getMobilePhone());
                }
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
        // kdyMobileList 为空，表示 list 为空，开单员手机号未开始收集
        if(KdyMobileList == null)
        {
            KdyMobileList = new ArrayList<String>();
            for (UserInfoVo userInfoVo : userInfoVoList)
            {
                //若是开单员角色则收集其手机号到 KdyMobileList 中，方便统一查询对应的工号
                if(RoleTypeEnum.ROLE_KDY.getRoleCode().equals(userInfoVo.getRoleCode()))
                {
                    KdyMobileList.add(userInfoVo.getMobilePhone());
                }
            }
        }
        //表示userinfovoList中存在开单员角色，则按照手机号列表查询对应工号
        Map<String,String> mobileUserIdMap = new HashMap<>();
        if(KdyMobileList.size() > 0)
        {
            List<Map> maps = userMapper.selectUserIdByMobile(KdyMobileList);
            for(Map map : maps)
            {
                mobileUserIdMap.put(map.get("PHONE_NO").toString(),map.get("USER_ID").toString());
            }
        }
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.string());
        String roleCode = null;
        for(UserInfoVo userInfoVo : userInfoVoList)
        {
            roleCode = userInfoVo.getRoleCode();
            if(RoleTypeEnum.ROLE_SJ_0.getRoleCode().equals(roleCode) ||
                    RoleTypeEnum.ROLE_SJ_1.getRoleCode().equals(roleCode))
            {
                userInfoVo.setOperatorTime(null);
                userInfoVo.setLastStockCode(null);
                userInfoVo.setGh(null);
            }
            else if(RoleTypeEnum.ROLE_JLY_KHB.getRoleCode().equals(roleCode) ||
                    RoleTypeEnum.ROLE_JLY_BC.getRoleCode().equals(roleCode) ||
                    RoleTypeEnum.ROLE_JLY_XC.getRoleCode().equals(roleCode) ||
                    RoleTypeEnum.ROLE_JLY_OTHER.getRoleCode().equals(roleCode))
            {
                userInfoVo.setCarNumList(null);
                userInfoVo.setGh(null);
            }
            else if(RoleTypeEnum.ROLE_KDY.getRoleCode().equals(roleCode))
            {
                userInfoVo.setCarNumList(null);
                userInfoVo.setOperatorTime(null);
                userInfoVo.setLastStockCode(null);
                if(mobileUserIdMap.size() > 0)
                {
                    String gh = mobileUserIdMap.get(userInfoVo.getMobilePhone()).toString();
                    userInfoVo.setGh(gh);
                }
            }
            else
            {
                userInfoVo.setCarNumList(null);
                userInfoVo.setOperatorTime(null);
                userInfoVo.setLastStockCode(null);
                userInfoVo.setGh(null);
            }
            String json = JSON.toJSONString(userInfoVo);
            String openId = userInfoVo.getOpenId();
            redisTemplate.opsForValue().set(openId,json);
        }
        LOGGER.info("结束存储用户信息至redis方法");
    }

    //    重写认证方法,实现自定义springsecurity用户认证（用户名密码登录）
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException
    {
        LOGGER.debug("进入通过用户名获取用户信息权限信息方法");
        PcUser pcUser = pcUserMapper.selectByUsername(userName);
        if (pcUser == null)
        {
            throw new UsernameNotFoundException("用户名不存在!");
        }

        LOGGER.debug("结束通过用户名获取用户信息权限信息方法");
        return pcUser;
    }

    @Override
    public PcUser loadUserByMobile(String mobile) throws UsernameNotFoundException
    {
        LOGGER.debug("进入通过手机号获取用户信息权限信息方法");
        PcUser pcUser = pcUserMapper.selectByMobile(mobile);
        if (pcUser == null)
        {
            throw new MobileNotFoundException("手机号未注册!");
        }

        LOGGER.debug("结束通过手机号获取用户信息权限信息方法");
        return pcUser;
    }

    @Override
    public int checkUserExistByMobile(String mobile) throws Exception
    {
        LOGGER.debug("开始通过手机号查询用户信息方法");
        Integer mobileExist;
        byte isDeleted = 0;
        byte isEnable = 1;
        byte isLocked = 0;
        mobileExist = pcUserMapper.checkUserByMobile(mobile,isDeleted,isEnable,isLocked);
        if(mobileExist == null)
        {
            mobileExist = 0;
        }
        LOGGER.debug("结束通过手机号查询用户信息方法");
        return mobileExist;
    }

    @Override
    public void updatePassword(String mobile, String newPassword) throws Exception
    {
        LOGGER.debug("进入更改pc端用户密码方法mobile={} newPassword={}",mobile,newPassword);
        pcUserMapper.updatePwdByMobile(mobile,bCryptPasswordEncoder.encode(newPassword));
        LOGGER.debug("结束更改pc端用户密码方法mobile={} newPassword={}",mobile,newPassword);

    }

    @Override
    public int checkUserRegByUsername(String username) throws Exception
    {
        LOGGER.debug("开始通过用户名查询pc端用户信息方法 username={}",username);
        Integer usernameExist;
        byte isDeleted = 0;
        usernameExist = pcUserMapper.selectRegUserByUsername(username,isDeleted);
        if(usernameExist == null)
        {
            usernameExist = 0;
        }
        LOGGER.debug("结束通过用户名查询pc端用户信息方法 username={}",username);
        return usernameExist;
    }

    @Override
    public int checkUserRegByMobile(String mobile) throws Exception
    {
        LOGGER.debug("开始通过手机号查询pc端用户信息方法 mobile={}",mobile);
        Integer mobileExist;
        byte isDeleted = 0;
        mobileExist = pcUserMapper.selectRegUserByMobile(mobile,isDeleted);
        if(mobileExist == null)
        {
            mobileExist = 0;
        }
        LOGGER.debug("结束通过手机号查询pc端用户信息方法 mobile={}",mobile);
        return mobileExist;
    }

    @Override
    public void addUser(String userName, String password, String mobile, String userRealName, Byte isDelete,
                        Byte isEnable, Byte isLocked, Long userCreate, String mesCode) throws Exception
    {
        LOGGER.debug("进入新增pc端用户方法 mobile={} mesCode={}",mobile,mesCode);
        String storedMesCode = mobileMapper.selectPcMesCodeByMobile(mobile);
        if(storedMesCode == null)
        {
            LOGGER.debug("pc端短信验证码过期 mobile={}",mobile);
            throw new MesCodeExpiredException("短信验证码过期，请点击重新发送");
        }
        if(!storedMesCode.equals(mesCode))
        {
            LOGGER.debug("pc端短信验证码错误 mobile={} storedMesCode={} mesCode={}",mobile,storedMesCode,mesCode);
            throw new MesCodeErrorException("pc端短信验证码错误");
        }
        else        //验证通过，将当前验证码改为无效状态
        {
            byte status = 0;
            LOGGER.debug("验证通过，将当前pc端验证码改为无效状态 mobile={} storedMesCode={}",mobile,storedMesCode);
            mobileMapper.updatePcMesCodeStatus(mobile,status,new Date());
        }
        PcUser pcUser = new PcUser();
        pcUser.setUserName(userName);
        pcUser.setPassword(bCryptPasswordEncoder.encode(password));
        pcUser.setMobile(mobile);
        pcUser.setUserRealName(userRealName);
        pcUser.setIsDeleted(isDelete);
        pcUser.setIsEnable(isEnable);
        pcUser.setIsLocked(isLocked);
        pcUser.setGmtCreate(new Date());
        pcUser.setGmtModified(new Date());
        pcUser.setUserCreate(userCreate);
        pcUser.setUserModified(userCreate);
        pcUser.setLastLoginTime(new Date());
        int id = pcUserMapper.insert(pcUser);
        System.out.println(id);
        LOGGER.debug("结束新增pc端用户方法 mobile={} mesCode={}",mobile,mesCode);
    }

    //通过手机号查询角色编码及工号
    public String[] queryRoleCodeByMobile(String mobile) throws Exception
    {
        List<Map> maps = userMapper.selectUserRoleByMobile(mobile);
        String roleCode = null;
        String gh = null;
        if (maps == null || maps.size() == 0)
        {
            roleCode = RoleTypeEnum.ROLE_SJ_0.getRoleCode();
        }
        else if (maps.size() == 1)
        {
            String item = maps.get(0).get("ITEM").toString().toUpperCase();
            switch (item)
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
                case "KDY":
                    roleCode = RoleTypeEnum.ROLE_KDY.getRoleCode();
                    break;
                default:
                    roleCode = RoleTypeEnum.ROLE_JLY_OTHER.getRoleCode();
            }
            gh = maps.get(0).get("USER_ID").toString();
        }
        else
        {
            roleCode = RoleTypeEnum.ROLE_ADMIN.getRoleCode();
        }
        String[] resArray = new String[2];
        resArray[0] = roleCode;
        resArray[1] = gh;
        return resArray;
    }
}
