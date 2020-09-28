package com.xyc.userc.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by 1 on 2020/9/26.
 */
public class UserInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String openId;

    private String mobilePhone;

    private List<CarNumInfoVo> carNumList;

    private String roleCode;

    private String lastStockCode;

    private Date operatorTime;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public List<CarNumInfoVo> getCarNumList() {
        return carNumList;
    }

    public void setCarNumList(List<CarNumInfoVo> carNumList) {
        this.carNumList = carNumList;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getLastStockCode() {
        return lastStockCode;
    }

    public void setLastStockCode(String lastStockCode) {
        this.lastStockCode = lastStockCode;
    }

    public Date getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Date operatorTime) {
        this.operatorTime = operatorTime;
    }
}
