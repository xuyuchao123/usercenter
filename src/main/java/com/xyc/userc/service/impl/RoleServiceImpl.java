package com.xyc.userc.service.impl;

import com.xyc.userc.dao.RoleMapper;
import com.xyc.userc.service.RoleService;
import com.xyc.userc.util.BusinessException;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.vo.DefaultUserRoleVo;
import com.xyc.userc.vo.RoleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

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

    @Override
    public List<DefaultUserRoleVo> getDefaultUserRole(String mobile, String page, String size)
    {
        LOGGER.debug("进入查询用户默认角色配置信息方法 mobile={} page={} size={}",mobile,page,size);
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

        List<DefaultUserRoleVo> colDefaultRoleVos = new ArrayList<>();
        if(defaultRoleVos.size() > 0)
        {
            colDefaultRoleVos.add(defaultRoleVos.get(0));
            String tmpJobNum = defaultRoleVos.get(0).getJobNum();
            StringJoiner stringJoiner = new StringJoiner(",");
            stringJoiner.add(defaultRoleVos.get(0).getRoleIds());
            DefaultUserRoleVo tmpDefaultRoleVo;
            for(int i = 1; i < defaultRoleVos.size(); i++)
            {
                tmpDefaultRoleVo = defaultRoleVos.get(i);
                if(tmpDefaultRoleVo.getJobNum().equals(tmpJobNum))
                {
                    stringJoiner.add(tmpDefaultRoleVo.getRoleIds());
                }
                else
                {
                    colDefaultRoleVos.get(colDefaultRoleVos.size()-1).setRoleIds(stringJoiner.toString());
                    //只取前20条返回
//                    if(colDefaultRoleVos.size() == 20)
//                    {
//                        break;
//                    }
                    tmpJobNum = tmpDefaultRoleVo.getJobNum();
                    colDefaultRoleVos.add(tmpDefaultRoleVo);
                    stringJoiner = new StringJoiner(",");
                    stringJoiner.add(tmpDefaultRoleVo.getRoleIds());
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
        LOGGER.debug("结束查询用户预置角色信息方法 mobile={} page={} size={}",mobile,page,size);
        return resList;
    }

    @Override
    public List<RoleVo> getAllRole()
    {
        LOGGER.debug("进入查询所有角色信息方法");
        List<RoleVo> roleVos = roleMapper.selectAllRole();
        LOGGER.debug("结束查询所有角色信息方法");
        return roleVos;
    }
}
