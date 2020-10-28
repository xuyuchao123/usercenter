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
    private String crossTime;
    private String forecastTime;
    private String zcrq;
    private String carInOutString;
    private String entranceName;
    private String hwmc;
    private String ysl;
    private String cdmc;


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

    public String getCrossTime() {
        return crossTime;
    }

    public void setCrossTime(String crossTime) {
        this.crossTime = crossTime;
    }

    public String getForecastTime() {
        return forecastTime;
    }

    public void setForecastTime(String forecastTime) {
        this.forecastTime = forecastTime;
    }

    public String getZcrq() {
        return zcrq;
    }

    public void setZcrq(String zcrq) {
        this.zcrq = zcrq;
    }

    public String getCarInOutString() {
        return carInOutString;
    }

    public void setCarInOutString(String carInOutString) {
        this.carInOutString = carInOutString;
    }

    public String getEntranceName() {
        return entranceName;
    }

    public void setEntranceName(String entranceName) {
        this.entranceName = entranceName;
    }

    public String getHwmc() {
        return hwmc;
    }

    public void setHwmc(String hwmc) {
        this.hwmc = hwmc;
    }

    public String getYsl() {
        return ysl;
    }

    public void setYsl(String ysl) {
        this.ysl = ysl;
    }

    public String getCdmc() {
        return cdmc;
    }

    public void setCdmc(String cdmc) {
        this.cdmc = cdmc;
    }
}
