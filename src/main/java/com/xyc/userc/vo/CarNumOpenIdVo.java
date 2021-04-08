package com.xyc.userc.vo;

import com.xyc.userc.entity.CarNumOpenId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by 1 on 2021/4/8.
 */
@ApiModel(value="车牌号信息返回对象类型")
public class CarNumOpenIdVo  implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String openId;
    private String carNum;
    private String engineNum;
    private String identNum;
    private String emissionStd;
    private String fleetName;
    private Date regDate;
    private String department;
    private Integer isEnabled;
    private Integer isDeleted;
    private String userCreate;
    private String userModified;
    private Date gmtCreate;
    private Date gmtModified;

    @ApiModelProperty(value="是否已上传行驶证，返回true表示已上传，返回false表示未上传")
    private boolean hasDrivingLicense;

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

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public String getUserModified() {
        return userModified;
    }

    public void setUserModified(String userModified) {
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

    public boolean isHasDrivingLicense() {
        return hasDrivingLicense;
    }

    public void setHasDrivingLicense(boolean hasDrivingLicense) {
        this.hasDrivingLicense = hasDrivingLicense;
    }
}
