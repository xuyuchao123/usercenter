package com.xyc.userc.vo;

import java.io.Serializable;

/**
 * Created by 1 on 2020/12/3.
 */
public class RoleVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String roleName;
    private String roleCode;
    private Integer preset;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Integer getPreset() {
        return preset;
    }

    public void setPreset(Integer preset) {
        this.preset = preset;
    }
}
