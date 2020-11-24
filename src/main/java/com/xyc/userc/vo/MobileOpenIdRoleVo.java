package com.xyc.userc.vo;

import java.io.Serializable;

/**
 * Created by 1 on 2020/9/24.
 */
public class MobileOpenIdRoleVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String roleCode;
    private Integer mobileOpenIdId;
    private Integer roleId;

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Integer getMobileOpenIdId() {
        return mobileOpenIdId;
    }

    public void setMobileOpenIdId(Integer mobileOpenIdId) {
        this.mobileOpenIdId = mobileOpenIdId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
