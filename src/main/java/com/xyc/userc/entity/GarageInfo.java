package com.xyc.userc.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1 on 2021/1/26.
 */
public class GarageInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String garageType;
    private String garageNum;
    private String garageName;
    private Integer maxLimit;
    private Date gmtCreate;
    private Date gmtModified;

    public GarageInfo(Integer id, String garageType, String garageNum, String garageName, Integer maxLimit, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.garageType = garageType;
        this.garageNum = garageNum;
        this.garageName = garageName;
        this.maxLimit = maxLimit;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGarageType() {
        return garageType;
    }

    public void setGarageType(String garageType) {
        this.garageType = garageType;
    }

    public String getGarageNum() {
        return garageNum;
    }

    public void setGarageNum(String garageNum) {
        this.garageNum = garageNum;
    }

    public String getGarageName() {
        return garageName;
    }

    public void setGarageName(String garageName) {
        this.garageName = garageName;
    }

    public Integer getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(Integer maxLimit) {
        this.maxLimit = maxLimit;
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
}
