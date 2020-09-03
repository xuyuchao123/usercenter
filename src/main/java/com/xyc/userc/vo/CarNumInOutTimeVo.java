package com.xyc.userc.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1 on 2020/9/3.
 */
public class CarNumInOutTimeVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String carNum;
    private String inOutTime;
    private String inOutType;

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getInOutTime() {
        return inOutTime;
    }

    public void setInOutTime(String inOutTime) {
        this.inOutTime = inOutTime;
    }

    public String getInOutType() {
        return inOutType;
    }

    public void setInOutType(String inOutType) {
        this.inOutType = inOutType;
    }
}
