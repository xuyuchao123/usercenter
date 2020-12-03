package com.xyc.userc.vo;

import java.io.Serializable;

/**
 * Created by 1 on 2020/12/3.
 */
public class DefaultRoleVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String jobName;
    private String mobile;
    private Integer roleId;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
