package com.xyc.userc.service.impl;

import com.xyc.userc.dao.RoleMapper;
import com.xyc.userc.service.RoleService;
import com.xyc.userc.vo.DefaultRoleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public List<DefaultRoleVo> getDefaultRole(String jobNum, String mobile, String roleName)
    {
        LOGGER.debug("进入查询默认角色配置信息方法 jobNum={} mobile={} roleName={}",jobNum,mobile,roleName);
        List<DefaultRoleVo> defaultRoleVos = roleMapper.selectDefaultRole(jobNum,mobile,roleName);
        LOGGER.debug("结束查询默认角色默认配置信息方法 jobNum={} mobile={} roleName={}",jobNum,mobile,roleName);
        return defaultRoleVos;
    }
}
