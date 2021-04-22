package com.xyc.userc.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by 1 on 2020/8/22.
 */
public class BlacklistVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String carNum;
    private String reason;
    private String createName;
    private String createMobile;
    private Long gmtCreate;

    public BlacklistVo() {
    }

    public BlacklistVo(String carNum, String reason, String createName, String createMobile, Long gmtCreate) {
        this.carNum = carNum;
        this.reason = reason;
        this.createName = createName;
        this.createMobile = createMobile;
        this.gmtCreate = gmtCreate;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateMobile() {
        return createMobile;
    }

    public void setCreateMobile(String createMobile) {
        this.createMobile = createMobile;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
