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
    public List<DefaultRoleVo> getDefaultRole(String jobNum, String mobile, String roleName)
    {
        LOGGER.debug("进入查询用户默认角色配置信息方法 jobNum={} mobile={} roleName={}",jobNum,mobile,roleName);
        List<DefaultRoleVo> defaultRoleVos = new ArrayList<>();
        defaultRoleVos = roleMapper.selectDefaultRole(jobNum,mobile,roleName);
        List<DefaultRoleVo> colDefaultRoleVos = new ArrayList<>();
        if(defaultRoleVos.size() > 0)
        {
            colDefaultRoleVos.add(defaultRoleVos.get(0));
            String tmpJobNum = defaultRoleVos.get(0).getJobNum();
            StringJoiner stringJoiner = new StringJoiner(",");
            stringJoiner.add(defaultRoleVos.get(0).getRoleIds());
            DefaultRoleVo tmpDefaultRoleVo;
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
                    tmpJobNum = tmpDefaultRoleVo.getJobNum();
                    colDefaultRoleVos.add(tmpDefaultRoleVo);
                    stringJoiner = new StringJoiner(",");
                    stringJoiner.add(tmpDefaultRoleVo.getRoleIds());
                }
            }
        }
        LOGGER.debug("结束查询用户默认角色配置信息方法 jobNum={} mobile={} roleName={}",jobNum,mobile,roleName);
        return colDefaultRoleVos;
    }
}
