package com.xyc.userc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by 1 on 2021/4/6.
 */
@ApiModel(value="违章信息请求参数对象类型")
public class ViolationInfoQueryVo
{
    @ApiModelProperty(value="开单方式", required = false, dataType="String",position = 0,example = "1：手动开单 2：自动开单 3")
    private String billingMethod;

    @ApiModelProperty(value="开单部门", required = false, dataType="String",position = 1)
    private String billingDepartment;

    @ApiModelProperty(value="开单时间", required = false, dataType="String",position = 2)
    private String billingTime;

    @ApiModelProperty(value="支付状态", required = false, dataType="String",position = 3)
    private String paymentStatus;

    @ApiModelProperty(value="开单序号", required = false, dataType="String",position = 4)
    private String billingSerialNumber;

    @NotNull(message = "page不能为空")
    @ApiModelProperty(value="当前页码", required = true, dataType="String",position = 5)
    private String page;

    @NotNull(message = "size不能为空")
    @ApiModelProperty(value="每页记录条数", required = true, dataType="String",position = 6)
    private String size;

    public String getBillingMethod() {
        return billingMethod;
    }

    public void setBillingMethod(String billingMethod) {
        this.billingMethod = billingMethod;
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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getBillingSerialNumber() {
        return billingSerialNumber;
    }

    public void setBillingSerialNumber(String billingSerialNumber) {
        this.billingSerialNumber = billingSerialNumber;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
