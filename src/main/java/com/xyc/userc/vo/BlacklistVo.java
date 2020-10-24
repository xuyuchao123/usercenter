package com.xyc.userc.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by 1 on 2020/8/22.
 */
public class BlacklistVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String name;
    private String mobile;
    private String reason;
    private String createName;
    private String createMobile;
    private Timestamp gmtCreate;

    public BlacklistVo(String name, String mobile, String reason, String createName, String createMobile, Date gmtCreate) {
        this.name = name;
        this.mobile = mobile;
        this.reason = reason;
        this.createName = createName;
        this.createMobile = createMobile;
        this.gmtCreate = gmtCreate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateMobile() {
        return createMobile;
    }

    public void setCreateMobile(String createMobile) {
        this.createMobile = createMobile;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
