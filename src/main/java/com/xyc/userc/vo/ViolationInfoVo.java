package com.xyc.userc.vo;

import java.io.Serializable;

/**
 * Created by 1 on 2021/4/1.
 */
public class ViolationInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String billDep;
    private Long billTime;
    private String billStaff;
    private String billNum;
    private String carNumber;
    private String fineReason;
    private String fineAmt;
    private String paymentStatus;

    public ViolationInfoVo(Integer id, String billDep, Long billTime, String billStaff, String billNum, String carNumber,
                           String fineReason, String fineAmt, String paymentStatus) {
        this.id = id;
        this.billDep = billDep;
        this.billTime = billTime;
        this.billStaff = billStaff;
        this.billNum = billNum;
        this.carNumber = carNumber;
        this.fineReason = fineReason;
        this.fineAmt = fineAmt;
        this.paymentStatus = paymentStatus;
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

    public Long getBillTime() {
        return billTime;
    }

    public void setBillTime(Long billTime) {
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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
