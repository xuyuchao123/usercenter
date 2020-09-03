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
    private String time;
    private String inOut;

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInOut() {
        return inOut;
    }

    public void setInOut(String inOut) {
        this.inOut = inOut;
    }
}
