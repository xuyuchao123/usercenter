package com.xyc.userc.service.impl;

import com.xyc.userc.dao.CarNumOpenIdMapper;
import com.xyc.userc.dao.MobileOpenIdMapper;
import com.xyc.userc.dao.RoleMapper;
import com.xyc.userc.entity.CarNumOpenId;
import com.xyc.userc.entity.MobileOpenId;
import com.xyc.userc.service.RedisService;
import com.xyc.userc.service.RoleService;
import com.xyc.userc.util.BusinessException;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.RoleTypeEnum;
import com.xyc.userc.util.UsercConstant;
import com.xyc.userc.vo.BindedUserRoleVo;
import com.xyc.userc.vo.DefaultUserRoleVo;
import com.xyc.userc.vo.RoleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by 1 on 2020/12/3.
 */
@Service("roleService")
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MobileOpenIdMapper mobileOpenIdMapper;

    @Autowired
    private CarNumOpenIdMapper carNumOpenIdMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public List<DefaultUserRoleVo> getDefaultUserRole(String mobile, String page, String size) throws Exception
    {
        LOGGER.info("进入查询用户预置角色信息方法 mobile={} page={} size={}",mobile,page,size);
        if(page == null)
        {
            throw new BusinessException(JsonResultEnum.PAGE_NOT_EXIST);
        }
        if(size == null)
        {
            throw new BusinessException(JsonResultEnum.SIZE_NOT_EXIST);
        }
        List<DefaultUserRoleVo> defaultRoleVos = new ArrayList<>();
        defaultRoleVos = roleMapper.selectDefaultUserRole(mobile);

        //获取已绑定角色编码和角色id的对应关系
        List<RoleVo> roleVos = roleMapper.selectAllRole();
        Map<String,String> roleCodeToIdMap = new HashMap<>();
        for(RoleVo roleVo : roleVos)
        {
            roleCodeToIdMap.put(roleVo.getRoleCode(),String.valueOf(roleVo.getId()));
        }

        List<DefaultUserRoleVo> colDefaultRoleVos = new ArrayList<>();
        String tmpRoleId = null;
        if(defaultRoleVos.size() > 0)
        {
            colDefaultRoleVos.add(defaultRoleVos.get(0));
            String tmpMobile = defaultRoleVos.get(0).getMobile();
            StringJoiner stringJoiner = new StringJoiner(",");
            tmpRoleId = roleCodeToIdMap.get(UsercConstant.ROLECODERELMAP_REV.get(defaultRoleVos.get(0).getRoleIds()));
            defaultRoleVos.get(0).setRoleIds(tmpRoleId);
            if(tmpRoleId != null)
            {
                stringJoiner.add(tmpRoleId);
            }
            DefaultUserRoleVo tmpDefaultRoleVo;
            for(int i = 1; i < defaultRoleVos.size(); i++)
            {
                tmpDefaultRoleVo = defaultRoleVos.get(i);
                tmpRoleId = roleCodeToIdMap.get(UsercConstant.ROLECODERELMAP_REV.get(tmpDefaultRoleVo.getRoleIds()));
                if(tmpDefaultRoleVo.getMobile().equals(tmpMobile))
                {
                    if(tmpRoleId != null)
                    {
                        stringJoiner.add(tmpRoleId);
                    }
                    if(i == defaultRoleVos.size()-1)
                    {
                        colDefaultRoleVos.get(colDefaultRoleVos.size()-1).setRoleIds(stringJoiner.toString());
                    }
                }
                else
                {
                    colDefaultRoleVos.get(colDefaultRoleVos.size()-1).setRoleIds(stringJoiner.toString());
                    tmpMobile = tmpDefaultRoleVo.getMobile();
                    colDefaultRoleVos.add(tmpDefaultRoleVo);
                    stringJoiner = new StringJoiner(",");
                    if(tmpRoleId != null)
                    {
                        stringJoiner.add(tmpRoleId);
                    }
                }
            }
        }
        int cnt = colDefaultRoleVos.size();
        int pageInt = Integer.valueOf(page);
        int sizeInt = Integer.valueOf(size);
        int pageCnt = cnt % sizeInt != 0 ? cnt / sizeInt + 1 : cnt / sizeInt;
        if(pageInt > pageCnt)
        {
            pageInt = pageCnt;
        }
        if(pageInt == 0)
        {
            pageInt = 1;
        }
        int start = (pageInt - 1) * sizeInt + 1;
        int end = start + sizeInt - 1;
        LOGGER.info("查询用户预置角色 start={},end={},cnt={}",start,end,cnt);
        List<DefaultUserRoleVo> colDefaultRoleVos_page = new ArrayList<>();
        for(int i = start-1; i <= end-1; i++)
        {
            if(i >= colDefaultRoleVos.size())
            {
                break;
            }
            colDefaultRoleVos_page.add(colDefaultRoleVos.get(i));
        }
        List resList = new ArrayList();
        resList.add(cnt);
        resList.add(pageInt);
        resList.add(sizeInt);
        resList.add(colDefaultRoleVos_page);
        LOGGER.info("结束查询用户预置角色信息方法 mobile={} page={} size={}",mobile,page,size);
        return resList;
    }

    @Override
    public List<BindedUserRoleVo> getBindedUserRole(String mobile, String page, String size) throws Exception
    {
        LOGGER.info("进入查询用户已绑定角色配置信息方法 mobile={} page={} size={}",mobile,page,size);
        if(page == null)
        {
            throw new BusinessException(JsonResultEnum.PAGE_NOT_EXIST);
        }
        if(size == null)
        {
            throw new BusinessException(JsonResultEnum.SIZE_NOT_EXIST);
        }
        List<BindedUserRoleVo> bindedUserRoleVos = new ArrayList<>();
        bindedUserRoleVos = roleMapper.selectBindedUserRole(mobile);

        List<BindedUserRoleVo> colBindedRoleVos = new ArrayList<>();
        if(bindedUserRoleVos.size() > 0)
        {
            colBindedRoleVos.add(bindedUserRoleVos.get(0));
            String tmpMobile = bindedUserRoleVos.get(0).getMobile();
            StringJoiner stringJoiner = new StringJoiner(",");
            stringJoiner.add(bindedUserRoleVos.get(0).getRoleIds());
            BindedUserRoleVo tmpBindedRoleVo;
            for(int i = 1; i < bindedUserRoleVos.size(); i++)
            {
                tmpBindedRoleVo = bindedUserRoleVos.get(i);
                if(tmpBindedRoleVo.getMobile().equals(tmpMobile))
                {
                    stringJoiner.add(tmpBindedRoleVo.getRoleIds());
                    if(i == bindedUserRoleVos.size()-1)
                    {
                        colBindedRoleVos.get(colBindedRoleVos.size()-1).setRoleIds(stringJoiner.toString());
                    }
                }
                else
                {
                    colBindedRoleVos.get(colBindedRoleVos.size()-1).setRoleIds(stringJoiner.toString());
                    tmpMobile = tmpBindedRoleVo.getMobile();
                    colBindedRoleVos.add(tmpBindedRoleVo);
                    stringJoiner = new StringJoiner(",");
                    stringJoiner.add(tmpBindedRoleVo.getRoleIds());
                }
            }
        }

        int cnt = colBindedRoleVos.size();
        int pageInt = Integer.valueOf(page);
        int sizeInt = Integer.valueOf(size);
        int pageCnt = cnt % sizeInt != 0 ? cnt / sizeInt + 1 : cnt / sizeInt;
        if(pageInt > pageCnt)
        {
            pageInt = pageCnt;
        }
        if(pageInt == 0)
        {
            pageInt = 1;
        }
        int start = (pageInt - 1) * sizeInt + 1;
        int end = start + sizeInt - 1;
        LOGGER.info("查询用户已绑定角色 start={},end={},cnt={}",start,end,cnt);

        List<BindedUserRoleVo> colBindedRoleVos_page = new ArrayList<>();
        for(int i = start-1; i <= end-1; i++)
        {
            if(i >= colBindedRoleVos.size())
            {
                break;
            }
            colBindedRoleVos_page.add(colBindedRoleVos.get(i));
        }
        List resList = new ArrayList();
        resList.add(cnt);
        resList.add(pageInt);
        resList.add(sizeInt);
        resList.add(colBindedRoleVos_page);
        LOGGER.info("进入查询用户已绑定角色配置信息方法 mobile={} page={} size={}",mobile,page,size);
        return resList;
    }

    @Override
    public List<RoleVo> getAllRole() throws Exception
    {
        LOGGER.info("进入查询所有角色信息方法");
        List<RoleVo> roleVos = roleMapper.selectAllRole();
        if(roleVos != null && roleVos.size() > 0)
        {
            for(RoleVo roleVo : roleVos)
            {
                if(UsercConstant.ROLECODERELMAP.get(roleVo.getRoleCode()) != null)
                {
                    roleVo.setPreset(1);
                }
                else
                {
                    roleVo.setPreset(0);
                }
            }
        }
        LOGGER.info("结束查询所有角色信息方法");
        return roleVos;
    }

    @Override
    public void addModifyDefaultUserRole(String jobNum, String mobile, String role) throws Exception
    {
        LOGGER.info("进入新增/修改用户预置角色信息方法 jobNum={} mobile={} role={}",jobNum,mobile,role);
        if(role == null || "".equals(role))
        {
            return;
        }
        String[] roleArr = role.split(",");
        if(roleArr == null || roleArr.length == 0)
        {
            return;
        }
        List<RoleVo> roleVos = roleMapper.selectAllRole();
        Map<Integer,String> roleIdToCodeMap = new HashMap<>();
        for(RoleVo roleVo : roleVos)
        {
            roleIdToCodeMap.put(roleVo.getId(),roleVo.getRoleCode());
        }
        //首先删除该手机号原有的预置角色
        roleMapper.deleteDefaultUserRole(mobile);
        //插入修改后的预置角色
        String preRoleCode = null;
        for(String s : roleArr)
        {
            preRoleCode = roleIdToCodeMap.get(Integer.valueOf(s));
            if(preRoleCode == null)
            {
                continue;
            }
            preRoleCode = UsercConstant.ROLECODERELMAP.get(preRoleCode);
            if(preRoleCode == null)
            {
                continue;
            }
            roleMapper.insertDefaultUserRole(jobNum,mobile,preRoleCode);
        }
        roleMapper.refreshDefaultUserRole();
        LOGGER.info("结束新增/修改用户预置角色信息方法 jobNum={} mobile={} role={}",jobNum,mobile,role);
        
    }

    @Override
    public void modifyBindedUserRole(String mobile, String role) throws Exception
    {
        LOGGER.info("开始修改用户已绑定角色信息方法 mobile={} role={}",mobile,role);
        if(role == null || "".equals(role))
        {
            return;
        }
        String[] roleArr = role.split(",");
        if(roleArr == null || roleArr.length == 0)
        {
            return;
        }
        //首先确认该手机号是否已绑定用户
        List<MobileOpenId> mobileOpenIds = mobileOpenIdMapper.selectByMobileOpenId(mobile,null);
        if(mobileOpenIds == null || mobileOpenIds.size() == 0)
        {
            throw new BusinessException(JsonResultEnum.MOBILE_NOT_BINDED);
        }

        //查询该手机号是否有绑定的车牌
        boolean isBindedCar = false;
        boolean isEnabledCar = false;
        List<CarNumOpenId> carNumOpenIds = carNumOpenIdMapper.selectByMobile(mobile);
        if(carNumOpenIds != null && carNumOpenIds.size() > 0)
        {
            LOGGER.info("该手机号有已绑定的车牌 mobile={}",mobile);
            isBindedCar = true;
            for(CarNumOpenId carNumOpenId : carNumOpenIds)
            {
                if(carNumOpenId.getIsEnabled() == 1)
                {
                    LOGGER.info("该手机号有已启用的车牌 mobile={} carNum={}",mobile,carNumOpenId.getCarNum());
                    isEnabledCar = true;
                    break;
                }
            }
        }
        else
        {
            LOGGER.info("该手机号没有已绑定的车牌 mobile={}",mobile);
        }

        //获取角色id与角色编码对应关系的map
        List<RoleVo> roleVos = roleMapper.selectAllRole();
        Map<Integer,String> roleIdToCodeMap = new HashMap<>();
        Map<String,Integer> roleCodeToIdMap = new HashMap<>();
        for(RoleVo roleVo : roleVos)
        {
            roleIdToCodeMap.put(roleVo.getId(),roleVo.getRoleCode());
            roleCodeToIdMap.put(roleVo.getRoleCode(),roleVo.getId());
        }

        if(role.indexOf(roleCodeToIdMap.get(RoleTypeEnum.ROLE_SJ_0.getRoleCode())) != -1)
        {
            //待修改的角色中包含 SJ0
            if(isEnabledCar)
            {
                LOGGER.info("该手机号有已启用的车牌，不能修改为 SJ0 角色 mobile={}",mobile);
                throw new BusinessException("该手机号有已启用的车牌，不能修改为 '" + RoleTypeEnum.ROLE_SJ_0.getRoleName() + " 角色!");
            }

        }
        if(role.indexOf(roleCodeToIdMap.get(RoleTypeEnum.ROLE_SJ_1.getRoleCode())) != -1)
        {
            //待修改的角色中包含 SJ1
            if(!isEnabledCar)
            {
                LOGGER.info("该手机号没有已启用的车牌，不能修改为 SJ1 角色 mobile={}",mobile);
                throw new BusinessException("该手机号没有已启用的车牌，不能修改为 '" + RoleTypeEnum.ROLE_SJ_1.getRoleName() + " 角色!");
            }
        }
        if(role.indexOf(roleCodeToIdMap.get(RoleTypeEnum.ROLE_HLDD.getRoleCode())) != -1)
        {
            //待修改的角色中包含 HLDD
            if(!isEnabledCar)
            {
                LOGGER.info("该手机号没有已启用的车牌，不能修改为 HLDD 角色 mobile={}",mobile);
                throw new BusinessException("该手机号没有已启用的车牌，不能修改为 '" + RoleTypeEnum.ROLE_HLDD.getRoleName() + " 角色!");
            }
        }

        //删除该手机号原先的角色
        for(MobileOpenId mobileOpenId : mobileOpenIds)
        {
            roleMapper.deleteUserRole(mobileOpenId.getId());
        }

        //插入修改后的角色
        Date date = new Date();
        Set<String> openIdSet = new HashSet<>();
        for(MobileOpenId mobileOpenId : mobileOpenIds)
        {
            openIdSet.add(mobileOpenId.getOpenId());
            for(String s : roleArr)
            {
                if(roleIdToCodeMap.get(Integer.valueOf(s)) == null)
                {
                    LOGGER.error("要修改的角色id不存在,或已删除 roleId={}",s);
                    throw new BusinessException(JsonResultEnum.ROLE_NOT_EXIST);
                }
                roleMapper.insertUserRole(mobileOpenId.getId(),Integer.valueOf(s),date,date);
            }
        }
        for(String openId : openIdSet)
        {
            LOGGER.info("开始更新redis中用户信息及车牌号信息 openId={}",openId);
            redisService.updateRedis(openId);
            LOGGER.info("结束redis中用户信息及车牌号信息 openId={}",openId);
        }

        String jobNum = "";
        //判断该手机号是否有预配置角色
        List<DefaultUserRoleVo> defaultUserRoleVos = roleMapper.selectDefaultUserRole(mobile);
        if(defaultUserRoleVos == null || defaultUserRoleVos.size() == 0)
        {
            LOGGER.info("该手机号没有预置角色 mobile={}",mobile);
        }
        else
        {
            jobNum = defaultUserRoleVos.get(0).getJobNum();
            if(jobNum == null)
            {
                jobNum = "";
            }
            LOGGER.info("该手机号有预置角色，开始删除预置角色 mobile={} jobNum={}",mobile);
            //删除该手机号原先的预置角色
            roleMapper.deleteDefaultUserRole(mobile);
        }

        //开始插入修改后的角色对应的预置角色
        String preRoleCode = null;
        for(String s : roleArr)
        {
            preRoleCode = roleIdToCodeMap.get(Integer.valueOf(s));
            if(preRoleCode == null)
            {
                continue;
            }
            preRoleCode = UsercConstant.ROLECODERELMAP.get(preRoleCode);
            if(preRoleCode == null)
            {
                continue;
            }
            roleMapper.insertDefaultUserRole(jobNum,mobile,preRoleCode);
        }
        roleMapper.refreshDefaultUserRole();
        LOGGER.info("结束修改用户已绑定角色信息方法 mobile={} role={}",mobile,role);

    }

    @Override
    public void removeDefaultUserRole(String mobile) throws Exception
    {
        LOGGER.info("开始删除用户预置角色信息方法 mobile={}",mobile);
        roleMapper.deleteDefaultUserRole(mobile);
        roleMapper.refreshDefaultUserRole();
        LOGGER.info("结束删除用户预置角色信息方法 mobile={}",mobile);
    }
}
