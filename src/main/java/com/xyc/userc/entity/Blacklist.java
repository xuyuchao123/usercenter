package com.xyc.userc.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1 on 2020/8/24.
 */
public class Blacklist implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String carNum;
    private String reason;
    private String userCreate;
    private Date gmtCreate;
    private String userModified;
    private Date gmtModified;
    private Integer isEnable;

    public Blacklist(Integer id, String carNum, String reason, String userCreate,
                     Date gmtCreate, String userModified, Date gmtModified, Integer isEnable) {
        this.id = id;
        this.carNum = carNum;
        this.reason = reason;
        this.userCreate = userCreate;
        this.gmtCreate = gmtCreate;
        this.userModified = userModified;
        this.gmtModified = gmtModified;
        this.isEnable = isEnable;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getUserModified() {
        return userModified;
    }

    public void setUserModified(String userModified) {
        this.userModified = userModified;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }
}
