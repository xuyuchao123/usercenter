package com.xyc.userc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by 1 on 2021/4/1.
 */
@ApiModel(value="违章信息返回对象类型")
public class ViolationInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="开单部门", position=0)
    private String billingDepartment;

    @ApiModelProperty(value="开单时间", position=1)
    private Long billingTime;

    @ApiModelProperty(value="开单人", position=2)
    private String openSingle;

    @ApiModelProperty(value="开单序号", position=3)
    private String billingSerialNumber;

    @ApiModelProperty(value="罚款事由", position=4)
    private String reasonForFine;

    @ApiModelProperty(value="罚款金额", position=5)
    private String theAmountOfTheFine;

    @ApiModelProperty(value="支付状态", position=6)
    private String paymentStatus;

    @ApiModelProperty(value="违章图片路径", position=7)
    private String illegalPictures;

    public ViolationInfoVo() {
    }

    public ViolationInfoVo(String billingDepartment, Long billingTime, String openSingle, String billingSerialNumber,
                           String reasonForFine, String theAmountOfTheFine, String paymentStatus, String illegalPictures) {
        this.billingDepartment = billingDepartment;
        this.billingTime = billingTime;
        this.openSingle = openSingle;
        this.billingSerialNumber = billingSerialNumber;
        this.reasonForFine = reasonForFine;
        this.theAmountOfTheFine = theAmountOfTheFine;
        this.paymentStatus = paymentStatus;
        this.illegalPictures = illegalPictures;
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

    public String getIllegalPictures() {
        return illegalPictures;
    }

    public void setIllegalPictures(String illegalPictures) {
        this.illegalPictures = illegalPictures;
    }
}
