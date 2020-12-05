package com.xyc.userc.service;

import com.xyc.userc.vo.BindedUserRoleVo;
import com.xyc.userc.vo.DefaultUserRoleVo;
import com.xyc.userc.vo.RoleVo;

import java.util.List;

/**
 * Created by 1 on 2020/12/3.
 */
public interface RoleService
{
    List<DefaultUserRoleVo> getDefaultUserRole(String mobile, String page, String size) throws Exception;

    List<RoleVo> getAllRole() throws Exception;

    void addModifyDefaultUserRole(String jobNum, String mobile, String role) throws Exception;

    void removeDefaultUserRole(String mobile) throws Exception;

    List<BindedUserRoleVo> getBindedUserRole(String mobile, String page, String size) throws Exception;

    void modifyBindedUserRole(String mobile, String role) throws Exception;
}
