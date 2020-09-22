package com.xyc.userc.entity;

import java.util.Date;

/**
 * Created by 1 on 2020/9/22.
 */
public class ShipReport
{
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String shipNum;
    private String name;
    private String reportDepartment;
    private Date travelTimeStart;
    private Date travelTimeEnd;
    private Integer isPass;
    private String userCreate;
    private Date gmtCreate;
    private String userModified;
    private Date gmtModified;
    private Integer isDeleted;

    public ShipReport(Integer id, String shipNum, String name, String reportDepartment,
                      Date travelTimeStart, Date travelTimeEnd, Integer isPass, String userCreate,
                      Date gmtCreate, String userModified, Date gmtModified, Integer isDeleted) {
        this.id = id;
        this.shipNum = shipNum;
        this.name = name;
        this.reportDepartment = reportDepartment;
        this.travelTimeStart = travelTimeStart;
        this.travelTimeEnd = travelTimeEnd;
        this.isPass = isPass;
        this.userCreate = userCreate;
        this.gmtCreate = gmtCreate;
        this.userModified = userModified;
        this.gmtModified = gmtModified;
        this.isDeleted = isDeleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShipNum() {
        return shipNum;
    }

    public void setShipNum(String shipNum) {
        this.shipNum = shipNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReportDepartment() {
        return reportDepartment;
    }

    public void setReportDepartment(String reportDepartment) {
        this.reportDepartment = reportDepartment;
    }

    public Date getTravelTimeStart() {
        return travelTimeStart;
    }

    public void setTravelTimeStart(Date travelTimeStart) {
        this.travelTimeStart = travelTimeStart;
    }

    public Date getTravelTimeEnd() {
        return travelTimeEnd;
    }

    public void setTravelTimeEnd(Date travelTimeEnd) {
        this.travelTimeEnd = travelTimeEnd;
    }

    public Integer getIsPass() {
        return isPass;
    }

    public void setIsPass(Integer isPass) {
        this.isPass = isPass;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getUserModified() {
        return userModified;
    }

    public void setUserModified(String userModified) {
        this.userModified = userModified;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}


