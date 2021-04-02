package com.xyc.userc.vo;

import java.io.Serializable;

/**
 * Created by 1 on 2021/4/1.
 */
public class ViolationInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String billingDepartment;
    private Long billingTime;
    private String openSingle;
    private String billingSerialNumber;
    private String reasonForFine;
    private String theAmountOfTheFine;
    private String paymentStatus;

    public ViolationInfoVo(String billingDepartment, Long billingTime, String openSingle, String billingSerialNumber,
                           String reasonForFine, String theAmountOfTheFine, String paymentStatus) {
        this.billingDepartment = billingDepartment;
        this.billingTime = billingTime;
        this.openSingle = openSingle;
        this.billingSerialNumber = billingSerialNumber;
        this.reasonForFine = reasonForFine;
        this.theAmountOfTheFine = theAmountOfTheFine;
        this.paymentStatus = paymentStatus;
    }

    public String getBillingDepartment() {
        return billingDepartment;
    }

    public void setBillingDepartment(String billingDepartment) {
        this.billingDepartment = billingDepartment;
    }

    public Long getBillingTime() {
        return billingTime;
    }

    public void setBillingTime(Long billingTime) {
        this.billingTime = billingTime;
    }

    public String getOpenSingle() {
        return openSingle;
    }

    public void setOpenSingle(String openSingle) {
        this.openSingle = openSingle;
    }

    public String getBillingSerialNumber() {
        return billingSerialNumber;
    }

    public void setBillingSerialNumber(String billingSerialNumber) {
        this.billingSerialNumber = billingSerialNumber;
    }

    public String getReasonForFine() {
        return reasonForFine;
    }

    public void setReasonForFine(String reasonForFine) {
        this.reasonForFine = reasonForFine;
    }

    public String getTheAmountOfTheFine() {
        return theAmountOfTheFine;
    }

    public void setTheAmountOfTheFine(String theAmountOfTheFine) {
        this.theAmountOfTheFine = theAmountOfTheFine;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
