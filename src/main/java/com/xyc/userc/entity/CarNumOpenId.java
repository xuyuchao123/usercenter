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
    private int userCreate;
    private int userModified;
    private Date gmtCreate;
    private Date gmtModified;

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

    public int getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(int userCreate) {
        this.userCreate = userCreate;
    }

    public int getUserModified() {
        return userModified;
    }

    public void setUserModified(int userModified) {
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
