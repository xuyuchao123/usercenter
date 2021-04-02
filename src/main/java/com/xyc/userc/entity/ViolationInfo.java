package com.xyc.userc.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1 on 2021/4/1.
 */
public class ViolationInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String billDep;
    private Date billTime;
    private String billStaff;
    private String billNum;
    private String carNumber;
    private String fineReason;
    private String fineAmt;
    private byte[] violationImg;
    private String paymentStatus;
    private Date gmtCreate;
    private Integer userCreate;
    private Date gmtModified;
    private Integer userModified;
    private Integer dataStatus;


    public ViolationInfo(Integer id, String billDep, Date billTime, String billStaff, String billNum, String carNumber,
                         String fineReason, String fineAmt, byte[] violationImg, String paymentStatus, Date gmtCreate,
                         Integer userCreate, Date gmtModified, Integer userModified, Integer dataStatus) {
        this.id = id;
        this.billDep = billDep;
        this.billTime = billTime;
        this.billStaff = billStaff;
        this.billNum = billNum;
        this.carNumber = carNumber;
        this.fineReason = fineReason;
        this.fineAmt = fineAmt;
        this.violationImg = violationImg;
        this.paymentStatus = paymentStatus;
        this.gmtCreate = gmtCreate;
        this.userCreate = userCreate;
        this.gmtModified = gmtModified;
        this.userModified = userModified;
        this.dataStatus = dataStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBillDep() {
        return billDep;
    }

    public void setBillDep(String billDep) {
        this.billDep = billDep;
    }

    public Date getBillTime() {
        return billTime;
    }

    public void setBillTime(Date billTime) {
        this.billTime = billTime;
    }

    public String getBillStaff() {
        return billStaff;
    }

    public void setBillStaff(String billStaff) {
        this.billStaff = billStaff;
    }

    public String getBillNum() {
        return billNum;
    }

    public void setBillNum(String billNum) {
        this.billNum = billNum;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getFineReason() {
        return fineReason;
    }

    public void setFineReason(String fineReason) {
        this.fineReason = fineReason;
    }

    public String getFineAmt() {
        return fineAmt;
    }

    public void setFineAmt(String fineAmt) {
        this.fineAmt = fineAmt;
    }

    public byte[] getViolationImg() {
        return violationImg;
    }

    public void setViolationImg(byte[] violationImg) {
        this.violationImg = violationImg;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(Integer userCreate) {
        this.userCreate = userCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getUserModified() {
        return userModified;
    }

    public void setUserModified(Integer userModified) {
        this.userModified = userModified;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }
}
