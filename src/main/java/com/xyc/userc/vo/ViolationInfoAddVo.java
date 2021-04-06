package com.xyc.userc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by 1 on 2021/4/6.
 */
@ApiModel(value="违章信息新增请求参数对象类型")
public class ViolationInfoAddVo
{
    @NotNull(message = "billingDepartment不能为空")
    @ApiModelProperty(value="开单部门", required = true, dataType="String")
    private String billingDepartment;

    @NotNull(message = "billingTime不能为空")
    @ApiModelProperty(value="开单时间", required = true, dataType="String")
    private String billingTime;

    @NotNull(message = "openSingle不能为空")
    @ApiModelProperty(value="开单人", required = true, dataType="String")
    private String openSingle;

    @NotNull(message = "billingSerialNumber不能为空")
    @ApiModelProperty(value="开单序号", required = true, dataType="String")
    private String billingSerialNumber;

    @NotNull(message = "theAmountOfTheFine不能为空")
    @ApiModelProperty(value="罚款金额", required = true, dataType="String")
    private String theAmountOfTheFine;

    @NotNull(message = "illegalPictures不能为空")
    @ApiModelProperty(value="违章图片", required = true, dataType="String")
    private String illegalPictures;

    @NotNull(message = "paymentStatus不能为空")
    @ApiModelProperty(value="支付状态", required = true, dataType="String")
    private String paymentStatus;

    @NotNull(message = "reasonForFine不能为空")
    @ApiModelProperty(value="罚款事由", required = true, dataType="String")
    private String reasonForFine;

    @Override
    public String toString() {
        return "ViolationInfoAddVo{" +
                "billingDepartment='" + billingDepartment + '\'' +
                ", billingTime='" + billingTime + '\'' +
                ", openSingle='" + openSingle + '\'' +
                ", billingSerialNumber='" + billingSerialNumber + '\'' +
                ", theAmountOfTheFine='" + theAmountOfTheFine + '\'' +
                ", illegalPictures='" + illegalPictures + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", reasonForFine='" + reasonForFine + '\'' +
                '}';
    }

    public String getBillingDepartment() {
        return billingDepartment;
    }

    public void setBillingDepartment(String billingDepartment) {
        this.billingDepartment = billingDepartment;
    }

    public String getBillingTime() {
        return billingTime;
    }

    public void setBillingTime(String billingTime) {
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

    public String getTheAmountOfTheFine() {
        return theAmountOfTheFine;
    }

    public void setTheAmountOfTheFine(String theAmountOfTheFine) {
        this.theAmountOfTheFine = theAmountOfTheFine;
    }

    public String getIllegalPictures() {
        return illegalPictures;
    }

    public void setIllegalPictures(String illegalPictures) {
        this.illegalPictures = illegalPictures;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getReasonForFine() {
        return reasonForFine;
    }

    public void setReasonForFine(String reasonForFine) {
        this.reasonForFine = reasonForFine;
    }
}
