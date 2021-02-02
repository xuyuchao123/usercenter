package com.xyc.userc.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1 on 2021/2/1.
 */
public class HallReportInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String openId;
    private String mobile;
    private String carNumber;
    private Date gmtCreate;
    private Integer dataStatus;
    private Integer orderId;
    private Integer lateTimes;
    private String BigLadingBillNo;
    private Date lastCalledTime;

    public HallReportInfo(Integer id, String openId, String mobile, String carNumber, Date gmtCreate, Integer dataStatus, Integer orderId, Integer lateTimes, String bigLadingBillNo, Date lastCalledTime) {
        this.id = id;
        this.openId = openId;
        this.mobile = mobile;
        this.carNumber = carNumber;
        this.gmtCreate = gmtCreate;
        this.dataStatus = dataStatus;
        this.orderId = orderId;
        this.lateTimes = lateTimes;
        BigLadingBillNo = bigLadingBillNo;
        this.lastCalledTime = lastCalledTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getLateTimes() {
        return lateTimes;
    }

    public void setLateTimes(Integer lateTimes) {
        this.lateTimes = lateTimes;
    }

    public String getBigLadingBillNo() {
        return BigLadingBillNo;
    }

    public void setBigLadingBillNo(String bigLadingBillNo) {
        BigLadingBillNo = bigLadingBillNo;
    }

    public Date getLastCalledTime() {
        return lastCalledTime;
    }

    public void setLastCalledTime(Date lastCalledTime) {
        this.lastCalledTime = lastCalledTime;
    }
}
