package com.xyc.userc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1 on 2020/10/10.
 */
@ApiModel(value="环保管控信息返回对象类型")
public class EnvInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="数据编号", position=0)
    private Integer id;

    @ApiModelProperty(value="业务部门", position=1)
    private String bumen;

    @ApiModelProperty(value="车牌号", position=2)
    private String carNumber;

    @ApiModelProperty(value="车辆识别代号", position=3)
    private String carVinNumber;

    @ApiModelProperty(value="发动机号", position=4)
    private String engineNumber;

    @ApiModelProperty(value="排放阶段", position=5)
    private String paif;

    @ApiModelProperty(value="通行时间", position=6)
    private String crossTime;

    @ApiModelProperty(value="预报时间", position=7)
    private String forecastTime;

    @ApiModelProperty(value="注册日期", position=8)
    private String zcrq;

    @ApiModelProperty(value="出入类型", position=9)
    private String carInOutString;

    @ApiModelProperty(value="通行门卫", position=10)
    private String entranceName;

    @ApiModelProperty(value="货物名称", position=11)
    private String hwmc;

    @ApiModelProperty(value="运输量", position=12)
    private String ysl;

    @ApiModelProperty(value="车队名称", position=13)
    private String cdmc;

    @ApiModelProperty(value="车辆图片url", position=14)
    private String vehicleUrl;


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

    public String getVehicleUrl() {
        return vehicleUrl;
    }

    public void setVehicleUrl(String vehicleUrl) {
        this.vehicleUrl = vehicleUrl;
    }
}
