package com.xyc.userc.service.impl;

import com.xyc.userc.dao.RoleMapper;
import com.xyc.userc.dao.UserMapper;
import com.xyc.userc.entity.Role;
import com.xyc.userc.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 1 on 2020/7/17.
 */
@Service("roleService")
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleMapper roleMapper;

    //通过请求路径查询对应权限
    @Override
    public List<Long> queryIdListByPath(String url) throws Exception
    {
        LOGGER.info("进入通过请求路径获取对应权限id列表方法 url={}",url);
        List<Long> roleIdList = roleMapper.selectIdsByPath(url);
        LOGGER.info("结束通过请求路径获取对应权限id列表方法 url={}",url);
        return roleIdList;
    }
}
