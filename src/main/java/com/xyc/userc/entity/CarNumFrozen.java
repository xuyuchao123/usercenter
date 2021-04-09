package com.xyc.userc.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1 on 2021/4/9.
 */
public class CarNumFrozen implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String carNum;

    private Integer frozenStatus;

    private Date startDate;

    private Date expireDate;

    private Date gmtCreate;

    private Date gmtModified;

    private Integer violationTimes;

    public CarNumFrozen(Integer id, String carNum, Integer frozenStatus, Date startDate, Date expireDate, Date gmtCreate, Date gmtModified, Integer violationTimes) {
        this.id = id;
        this.carNum = carNum;
        this.frozenStatus = frozenStatus;
        this.startDate = startDate;
        this.expireDate = expireDate;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
        this.violationTimes = violationTimes;
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

    public Integer getFrozenStatus() {
        return frozenStatus;
    }

    public void setFrozenStatus(Integer frozenStatus) {
        this.frozenStatus = frozenStatus;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
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

    public Integer getViolationTimes() {
        return violationTimes;
    }

    public void setViolationTimes(Integer violationTimes) {
        this.violationTimes = violationTimes;
    }
}
