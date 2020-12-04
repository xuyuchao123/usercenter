package com.xyc.userc.vo;

import java.io.Serializable;

/**
 * Created by 1 on 2020/12/3.
 */
public class DefaultUserRoleVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String jobNum;
    private String mobile;
    private String roleIds;

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }
}
