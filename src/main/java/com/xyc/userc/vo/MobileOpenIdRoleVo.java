package com.xyc.userc.vo;

/**
 * Created by 1 on 2020/9/24.
 */
public class MobileOpenIdRoleVo
{
    private static final long serialVersionUID = 1L;

    private String roleCode;
    private Integer mobileOpenIdId;

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
}
