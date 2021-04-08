package com.xyc.userc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by 1 on 2021/4/8.
 */
@ApiModel(value="违章信息新增请求参数对象类型")
public class CarNumInfoAddVo
{
    @NotNull(message = "carNum不能为空")
    @ApiModelProperty(value="车牌号", required = true, dataType="String")
    private String carNum;

    @NotNull(message = "engineNum不能为空")
    @ApiModelProperty(value="发动机号", required = true, dataType="String")
    private String engineNum;

    @NotNull(message = "identNum不能为空")
    @ApiModelProperty(value="车辆识别号", required = true, dataType="String")
    private String identNum;

    @NotNull(message = "emissionStd不能为空")
    @ApiModelProperty(value="排放标准", required = true, dataType="String")
    private String emissionStd;

    @NotNull(message = "fleetName不能为空")
    @ApiModelProperty(value="车队名称", required = true, dataType="String")
    private String fleetName;

    @NotNull(message = "regDate不能为空")
    @ApiModelProperty(value="注册日期", required = true, dataType="String")
    private String regDate;

    @NotNull(message = "department不能为空")
    @ApiModelProperty(value="业务管理部门", required = true, dataType="String")
    private String department;

    @ApiModelProperty(value="行驶证url", required = false, dataType="String")
    private String drivingLicense;

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getEngineNum() {
        return engineNum;
    }

    public void setEngineNum(String engineNum) {
        this.engineNum = engineNum;
    }

    public String getIdentNum() {
        return identNum;
    }

    public void setIdentNum(String identNum) {
        this.identNum = identNum;
    }

    public String getEmissionStd() {
        return emissionStd;
    }

    public void setEmissionStd(String emissionStd) {
        this.emissionStd = emissionStd;
    }

    public String getFleetName() {
        return fleetName;
    }

    public void setFleetName(String fleetName) {
        this.fleetName = fleetName;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    @Override
    public String toString() {
        return "CarNumInfoAddVo{" +
                "carNum='" + carNum + '\'' +
                ", engineNum='" + engineNum + '\'' +
                ", identNum='" + identNum + '\'' +
                ", emissionStd='" + emissionStd + '\'' +
                ", fleetName='" + fleetName + '\'' +
                ", regDate='" + regDate + '\'' +
                ", department='" + department + '\'' +
                ", drivingLicense='" + drivingLicense + '\'' +
                '}';
    }


}
