package com.xyc.userc.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1 on 2021/2/3.
 */
public class HallReportComment implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    private String openId;
    private String carNum;
    private String comment;
    private Date gmtCreate;
    private String bigLadingBillNo;

    public HallReportComment(String id, String openId, String carNum, String comment, Date gmtCreate, String bigLadingBillNo) {
        this.id = id;
        this.openId = openId;
        this.carNum = carNum;
        this.comment = comment;
        this.gmtCreate = gmtCreate;
        this.bigLadingBillNo = bigLadingBillNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getBigLadingBillNo() {
        return bigLadingBillNo;
    }

    public void setBigLadingBillNo(String bigLadingBillNo) {
        this.bigLadingBillNo = bigLadingBillNo;
    }
}
