package com.xyc.userc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by 1 on 2021/1/26.
 */
@ApiModel(value="库位配置信息返回对象类型")
public class GarageInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="库位类型", position=0)
    private String garageType;

    @ApiModelProperty(value="库位编号", position=1)
    private String garageNum;

    @ApiModelProperty(value="库位名称", position=2)
    private String garageName;

    @ApiModelProperty(value="最大限制数", position=4)
    private Integer maxLimit;

    @ApiModelProperty(value="库区编号", position=5)
    private String location;

    public String getGarageType() {
        return garageType;
    }

    public void setGarageType(String garageType) {
        this.garageType = garageType;
    }

    public String getGarageNum() {
        return garageNum;
    }

    public void setGarageNum(String garageNum) {
        this.garageNum = garageNum;
    }

    public String getGarageName() {
        return garageName;
    }

    public void setGarageName(String garageName) {
        this.garageName = garageName;
    }

    public Integer getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(Integer maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
