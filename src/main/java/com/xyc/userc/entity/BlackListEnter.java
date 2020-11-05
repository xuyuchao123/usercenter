package com.xyc.userc.entity;

import java.io.Serializable;

/**
 * Created by 1 on 2020/11/5.
 */
public class BlackListEnter implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String carNum;
    private String enterDate;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
