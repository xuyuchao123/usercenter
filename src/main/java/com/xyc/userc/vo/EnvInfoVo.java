package com.xyc.userc.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1 on 2020/10/10.
 */
public class EnvInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String bumen;
    private String carNumber;
    private String carVinNumber;
    private String engineNumber;
    private String paif;
    private Date crossTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBumen() {
        return bumen;
    }

    public void setBumen(String bumen) {
        this.bumen = bumen;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarVinNumber() {
        return carVinNumber;
    }

    public void setCarVinNumber(String carVinNumber) {
        this.carVinNumber = carVinNumber;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getPaif() {
        return paif;
    }

    public void setPaif(String paif) {
        this.paif = paif;
    }

    public Date getCrossTime() {
        return crossTime;
    }

    public void setCrossTime(Date crossTime) {
        this.crossTime = crossTime;
    }
}
