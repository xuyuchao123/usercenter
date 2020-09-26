package com.xyc.userc.vo;

import java.io.Serializable;

/**
 * Created by 1 on 2020/9/26.
 */
public class CarNumInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String carNum;
    private Integer isEnable;

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }
}
