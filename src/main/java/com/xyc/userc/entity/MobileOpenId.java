package com.xyc.userc.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1 on 2020/8/18.
 */
public class MobileOpenId implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String mobile;

    private String openId;

    private String userCreate;

    private Date gmtCreate;

    public MobileOpenId(Integer id, String mobile, String openId, String userCreate, Date gmtCreate)
    {
        this.id = id;
        this.mobile = mobile;
        this.openId = openId;
        this.userCreate = userCreate;
        this.gmtCreate = gmtCreate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
