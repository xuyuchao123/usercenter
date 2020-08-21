package com.xyc.userc.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1 on 2020/8/19.
 */
public class CarNumOpenId implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int id;
    private String openId;
    private String carNum;
    private int isDeleted;
    private String userCreate;
    private String userModified;
    private Date gmtCreate;
    private Date gmtModified;

    public CarNumOpenId(String openId, String carNum, int isDeleted, String userCreate, String userModified, Date gmtCreate, Date gmtModified) {
        this.openId = openId;
        this.carNum = carNum;
        this.isDeleted = isDeleted;
        this.userCreate = userCreate;
        this.userModified = userModified;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public String getUserModified() {
        return userModified;
    }

    public void setUserModified(String userModified) {
        this.userModified = userModified;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}
