package com.xyc.userc.service;

import com.xyc.userc.vo.DefaultUserRoleVo;
import com.xyc.userc.vo.RoleVo;

import java.util.List;

/**
 * Created by 1 on 2020/12/3.
 */
public interface RoleService
{
    List<DefaultUserRoleVo> getDefaultUserRole(String mobile, String page, String size);

    List<RoleVo> getAllRole();
}
