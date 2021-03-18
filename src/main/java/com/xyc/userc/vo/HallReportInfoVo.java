package com.xyc.userc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1 on 2021/2/3.
 */
@ApiModel(value="大厅报道排队信息返回对象类型")
public class HallReportInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="排队序号", position=0)
    private Integer id;

    @ApiModelProperty(value="手机号", position=1)
    private String mobile;

    @ApiModelProperty(value="车牌号", position=2)
    private String carNumber;

    @ApiModelProperty(value="迟到次数", position=3)
    private Integer lateTimes;

    @ApiModelProperty(value="库区编码", position=4)
    private String location;

    public HallReportInfoVo(Integer id, String mobile, String carNumber, Integer lateTimes,String location)
    {
        this.id = id;
        this.mobile = mobile;
        this.carNumber = carNumber;
        this.lateTimes = lateTimes;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Integer getLateTimes() {
        return lateTimes;
    }

    public void setLateTimes(Integer lateTimes) {
        this.lateTimes = lateTimes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
