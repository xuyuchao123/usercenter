package com.xyc.userc.service;

import com.xyc.userc.vo.DefaultRoleVo;

import java.util.List;

/**
 * Created by 1 on 2020/12/3.
 */
public interface RoleService
{
    List<DefaultRoleVo> getDefaultRole(String jobNum, String mobile, String roleName);
}
