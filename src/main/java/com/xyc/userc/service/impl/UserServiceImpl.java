package com.xyc.userc.service.impl;

import com.alibaba.fastjson.JSON;
import com.xyc.userc.dao.*;
import com.xyc.userc.entity.*;
import com.xyc.userc.security.MesCodeErrorException;
import com.xyc.userc.security.MesCodeExpiredException;
import com.xyc.userc.security.MobileNotFoundException;
import com.xyc.userc.service.RedisService;
import com.xyc.userc.service.UserService;
import com.xyc.userc.util.BusinessException;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.RoleTypeEnum;
import com.xyc.userc.vo.CarNumInfoVo;
import com.xyc.userc.vo.CarNumOpenIdVo;
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

    @Autowired
    private RedisService redisService;


    @Override
    public User getUser(User user) throws Exception
    {
        String openId = user.getOpenId();
        LOGGER.info("进入获取用户信息方法 openid：{}",openId);
        List<Role> roleList = roleMapper.selectByOpenId(openId);
        user.setRoles(roleList);
        String enabledCarNum = carNumOpenIdMapper.selectEnabledCarInfo(openId);
        if(enabledCarNum != null) {
            LOGGER.info("获取到用户已启用的车牌号 carNum={}", enabledCarNum);
            user.setCarNum(enabledCarNum);
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
            List<Map> maps = mobileOpenIdMapper.selectByMobileOpenIdRole(null,openId);
            List<String> roleCodeList = null;
            String gh = null;
            List<Role> roles = new ArrayList<>();
            boolean isUpdate = false;
            Date date = new Date();
            String roleCodeStrs = "";
            if(maps != null && maps.size() > 0)
            {
                isUpdate = true;
                String mobile_ori = maps.get(0).get("MOBILE").toString();
                LOGGER.info("当前openId已绑定手机号 mobile_ori={} openId={}", mobile_ori,openId);
//                if(mobile_ori.equals(mobile))
//                {
//                    LOGGER.error("当前openId已绑定该手机号，不能重复绑定 mobile={} openId={}", mobile,openId);
//                    throw new BusinessException(JsonResultEnum.MOBILE_BINDED);
//                }
                if(!mobile_ori.equals(mobile))
                {
                    LOGGER.info("开始更新当前openId绑定的手机号 mobile_ori={} mobile={} openId={}", mobile_ori,mobile,openId);
                    mobileOpenIdMapper.updateMobile(mobile, openId);
                }
                List<Object> resList = queryRoleCodeByMobile(mobile);
                roleCodeList = (List<String>)resList.get(0);
//                String roleCodeStrs = "";
                for(String roleCode : roleCodeList)
                {
                    roleCodeStrs += roleCode;
                    roleCodeStrs += "=";
                }
                roleCodeStrs = roleCodeStrs.substring(0,roleCodeStrs.length()-1);
                gh = (String)resList.get(1);
                List<String> oriRoleCodeList = new ArrayList<>();
                String oriRoleCodeStrs = "";
                for(Map map : maps)
                {
                    String oriRoleCode = map.get("ROLE_CODE").toString();
                    oriRoleCodeList.add(oriRoleCode);
                    oriRoleCodeStrs += oriRoleCode;
                    oriRoleCodeStrs += " ";
                }
                LOGGER.info("当前手机号：{},对应的角色编码：{}, 工号：{} 原来的角色编码：{}",mobile,roleCodeStrs,gh,oriRoleCodeStrs);

                //若新手机号对应的角色司机0
                if(roleCodeList.size() == 1 && roleCodeList.get(0).equals(RoleTypeEnum.ROLE_SJ_0.getRoleCode()))
                {
                    //该openId原先对应的角色为司机1，或者为 HLDD， 则需要修改绑定的手机号之后角色仍为司机1
                    if((oriRoleCodeList.size() == 1 && oriRoleCodeList.get(0).equals(RoleTypeEnum.ROLE_SJ_1.getRoleCode()))
                            || oriRoleCodeList.contains(RoleTypeEnum.ROLE_HLDD.getRoleCode()))
                    {
                        LOGGER.info("新手机号对应的角色为司机0，且该openId原先对应的角色为司机1 或者为 HLDD，修改绑定的手机号之后角色仍为司机1");
                        roleCodeList.set(0,RoleTypeEnum.ROLE_SJ_1.getRoleCode());
                    }
                }

//                //若新手机号对应的角色为司机0，且该openId原先对应的角色为司机1，则修改绑定的手机号之后角色仍然为司机1
//                if(roleCodeList.size() == 1 && roleCodeList.get(0).equals(RoleTypeEnum.ROLE_SJ_0.getRoleCode())
//                        && oriRoleCodeList.size() == 1 && oriRoleCodeList.get(0).equals(RoleTypeEnum.ROLE_SJ_1.getRoleCode()))
//                {
//                    LOGGER.info("新手机号对应的角色为司机0，且该openId原先对应的角色为司机1，修改绑定的手机号之后角色仍为司机1");
//                    roleCodeList.set(0,RoleTypeEnum.ROLE_SJ_1.getRoleCode());
//                }

                //若新手机号对应角色包含 HLDD
                if(roleCodeList.contains(RoleTypeEnum.ROLE_HLDD.getRoleCode()))
                {
                    //该openId原先对应的角色不包含 HLDD,和 SJ1 则绑定的手机号之后的角色列表中的 HLDD 角色应修改为 SJ0
                    if(!oriRoleCodeList.contains(RoleTypeEnum.ROLE_HLDD.getRoleCode()) && !oriRoleCodeList.contains(RoleTypeEnum.ROLE_SJ_1.getRoleCode()))
                    {
                        LOGGER.info("新手机号对应的角色包含HLDD，该openId原先对应的角色不包含 HLDD,和 SJ1 绑定手机号之后的角色列表中的 HLDD 角色应修改为 SJ0");
                        {
                            int  hlddIdx = roleCodeList.indexOf(RoleTypeEnum.ROLE_HLDD.getRoleCode());
                            roleCodeList.set(hlddIdx,RoleTypeEnum.ROLE_SJ_0.getRoleCode());
                        }
                    }
                }

                Integer mobileOpenIdId = Integer.valueOf(maps.get(0).get("ID").toString());
                //删除原先手机号对应的角色关联关系
                roleMapper.deleteUserRole(mobileOpenIdId);
                for(String roleCode : roleCodeList)
                {
                    Role role = roleMapper.selectByRoleCode(roleCode);
                    roles.add(role);
                    //插入用户与角色关联表
                    roleMapper.insertUserRole(mobileOpenIdId, role.getId(), date, date);
                }
                //若原角色为司机或者HLDD，重新绑定后角色不为司机和HLDD，则需判断该oopenId下是否有绑定的车牌号，有车牌号则删除
                if((oriRoleCodeList.contains(RoleTypeEnum.ROLE_SJ_1.getRoleCode())
                        || oriRoleCodeList.contains(RoleTypeEnum.ROLE_HLDD.getRoleCode())
                        || oriRoleCodeList.contains(RoleTypeEnum.ROLE_SJ_0.getRoleCode()))
                        && (!roleCodeList.contains(RoleTypeEnum.ROLE_SJ_1.getRoleCode()) && !roleCodeList.contains(RoleTypeEnum.ROLE_HLDD.getRoleCode())
                            && !roleCodeList.contains(RoleTypeEnum.ROLE_SJ_0.getRoleCode())))
                {
                    LOGGER.info("原角色为司机或者HLDD，重新绑定后角色不为司机和HLDD，开始判断该oopenId下是否有绑定的车牌号 openId={}",openId);
                    List<CarNumOpenIdVo> carNumOpenIdVos =  carNumOpenIdMapper.selectByOpenId(openId);
                    if(carNumOpenIdVos != null && carNumOpenIdVos.size() > 0)
                    {
                        LOGGER.info("该oopenId下有绑定的车牌号,开始删除这些车牌号的绑定关系 openId={},车牌号数量：{}",openId,carNumOpenIdVos.size());
                        int delCnt = carNumOpenIdMapper.deleteByCarNumOpenId(null,openId);
                        LOGGER.info("成功删除 {} 个车牌号",delCnt);
                    }
                }
            }
            else
            {
                LOGGER.info("当前openId未绑定手机号openId={}", openId);
                int insertCnt = mobileOpenIdMapper.insertMobileOpenId(mobileOpenId);
                int id = mobileOpenId.getId();
                List<Object> resList = queryRoleCodeByMobile(mobile);
                roleCodeList = (List<String>)resList.get(0);
                gh = (String)resList.get(1);
                for(String roleCode : roleCodeList)
                {
                    //若当前遍历的角色为 HLDD 则需将其修改为 SJ0
                    if(roleCode.equals(RoleTypeEnum.ROLE_HLDD.getRoleCode()))
                    {
                        roleCode = RoleTypeEnum.ROLE_SJ_0.getRoleCode();
                    }
                    Role role = roleMapper.selectByRoleCode(roleCode);
                    roles.add(role);
                    //插入用户与角色关联表
                    roleMapper.insertUserRole(id, role.getId(), date, date);
                    roleCodeStrs += roleCode;
                    roleCodeStrs += "=";
                }
                roleCodeStrs = roleCodeStrs.substring(0,roleCodeStrs.length()-1);
                LOGGER.info("当前手机号：{},对应的角色编码：{}, 工号：{}",mobile,roleCodeStrs,gh);
            }

            LOGGER.info("开始将用户绑定信息存入redis openId={} mobile={}", openId, mobile);
            UserInfoVo userInfoVo = new UserInfoVo();
            userInfoVo.setOpenId(openId);
            userInfoVo.setMobilePhone(mobile);
            userInfoVo.setRoleCode(roleCodeStrs);
            if(roleCodeList.contains(RoleTypeEnum.ROLE_SJ_0.getRoleCode()) || roleCodeList.contains(RoleTypeEnum.ROLE_SJ_1.getRoleCode())
                || roleCodeList.contains(RoleTypeEnum.ROLE_HLDD.getRoleCode()) )
            {
                if(!isUpdate)
                {
                    LOGGER.info("当前openId未绑定手机号,无需获取对应的车牌号列表");
                    userInfoVo.setCarNumList(new ArrayList<CarNumInfoVo>());
                }
                else
                {
                    LOGGER.info("当前openId已绑定手机号,需要获取对应的车牌号列表");
                    List<CarNumInfoVo> carNumInfoVoList = new ArrayList<>();
                    carNumInfoVoList = carNumOpenIdMapper.selectCarNumInfoVo(openId);
                    userInfoVo.setCarNumList(carNumInfoVoList);
                }
            }
            if (roleCodeList.contains(RoleTypeEnum.ROLE_JLY_BC.getRoleCode()) || roleCodeList.contains(RoleTypeEnum.ROLE_JLY_OTHER.getRoleCode()))
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
//            redisTemplate.setKeySerializer(RedisSerializer.string());
//            redisTemplate.setValueSerializer(RedisSerializer.string());
            redisTemplate.opsForValue().set(openId, JSON.toJSONString(userInfoVo));
//
            //构建user对象返回
            User user = new User();
            user.setOpenId(openId);
            user.setMobilePhone(mobile);
            user.setRoles(roles);
            LOGGER.info("结束绑定手机号方法 mobile={} nickName={} mesCode={} openid={}",mobile,nickName,mesCode,openId);
            return user;
        }

    }


    @Override
    public void quickRegister(String carNum, String mobile, String openId) throws Exception
    {
        LOGGER.info("开始快速注册方法 carNum={} mobile={} openId={}",carNum,mobile,openId);
        LOGGER.info("开始查询当前openId是否已绑定手机号openId={}",openId);
        List<Map> maps = mobileOpenIdMapper.selectByMobileOpenIdRole(null,openId);
        Date date = new Date();
        if(maps != null && maps.size() > 0)
        {
            LOGGER.info("openid 已绑定手机号 openid={}",openId);
            List<CarNumOpenIdVo> carNumOpenIdVos = carNumOpenIdMapper.selectByOpenId(openId);
            boolean needInsert = true;
            if(carNumOpenIdVos != null && carNumOpenIdVos.size() > 0)
            {
                //遍历已绑定的车牌号，若其中包含 carNum 则无需再次绑定
                for(CarNumOpenIdVo carNumOpenIdVo : carNumOpenIdVos)
                {
                    if(carNumOpenIdVo.getCarNum().equals(carNum))
                    {
                        needInsert = false;
                        break;
                    }
                }
            }
            if(needInsert)
            {
                LOGGER.info("开始生成车牌号绑定关系");
                CarNumOpenId carNumOpenId = new CarNumOpenId(null,openId,carNum,"","","","",null,"",0,0,openId,openId,date,date);
                carNumOpenIdMapper.insert(carNumOpenId);
            }
        }
        else
        {
            LOGGER.info("openid 未绑定手机号 openid={}",openId);
            LOGGER.info("开始生成手机号openid绑定关系");
            MobileOpenId mobileOpenId = new MobileOpenId(null,mobile,"",openId,openId,date);
            int insertCnt = mobileOpenIdMapper.insertMobileOpenId(mobileOpenId);
            int id = mobileOpenId.getId();
            LOGGER.info("开始生成用户权限");
            Role role = roleMapper.selectByRoleCode(RoleTypeEnum.ROLE_SJ_1.getRoleCode());
            roleMapper.insertUserRole(id, role.getId(), date, date);
            LOGGER.info("开始生成车牌号绑定关系,并启用车牌号");
            CarNumOpenId carNumOpenId = new CarNumOpenId(null,openId,carNum,"","","","",null,"",1,0,openId,openId,date,date);
            carNumOpenIdMapper.insert(carNumOpenId);
        }
        LOGGER.info("开始更新redis中用户信息及车牌号信息openId={}",openId);
        redisService.updateRedis(openId);
        LOGGER.info("结束快速注册方法 carNum={} mobile={} openId={}",carNum,mobile,openId);
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
    public List<Object> queryRoleCodeByMobile(String mobile) throws Exception
    {
        List<Map> maps = userMapper.selectUserRoleByMobile(mobile);
        List<String> roleCodeList = new ArrayList<>();
        String gh = null;
        if (maps == null || maps.size() == 0)
        {
            roleCodeList.add(RoleTypeEnum.ROLE_SJ_0.getRoleCode());
        }
        else
        {
            for(Map map : maps)
            {
                String item = map.get("ITEM").toString().toUpperCase();
                String roleCode = null;
                switch (item)
                {
                    case "XC":
                        roleCode = RoleTypeEnum.ROLE_JLY_XC.getRoleCode();
                        break;
                    case "BC":
                        roleCode = RoleTypeEnum.ROLE_JLY_BC.getRoleCode();
                        break;
                    case "HP":
                        roleCode = RoleTypeEnum.ROLE_JLY_KHB.getRoleCode();
                        break;
                    case "KDY":
                        roleCode = RoleTypeEnum.ROLE_KDY.getRoleCode();
                        break;
                    case "HBGK":
                        roleCode = RoleTypeEnum.ROLE_HBGK.getRoleCode();
                        break;
                    case "HLDD":
                        roleCode = RoleTypeEnum.ROLE_HLDD.getRoleCode();
                        break;
                    default:
                        roleCode = RoleTypeEnum.ROLE_JLY_OTHER.getRoleCode();
                }
                roleCodeList.add(roleCode);
            }
            gh = maps.get(0).get("USER_ID").toString();
        }
        List<Object> resList = new ArrayList<>();
        resList.add(roleCodeList);
        resList.add(gh);
        return resList;
    }



}
