package com.xyc.userc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1 on 2020/11/17.
 */
@ApiModel(value="船户通行息返回对象类型")
public class ShipInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="id号", position=0)
    private String shipNum;

    @ApiModelProperty(value="船民电话", position=1)
    private String mobile;

    @ApiModelProperty(value="货名", position=2)
    private String cargoName;

    @ApiModelProperty(value="姓名", position=3)
    private String name;

    @ApiModelProperty(value="创建时间", position=4)
    private Long gmtCreate;

    @ApiModelProperty(value="身份证号", position=5)
    private String idNumber;

    public String getShipNum() {
        return shipNum;
    }

    public void setShipNum(String shipNum) {
        this.shipNum = shipNum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
}
