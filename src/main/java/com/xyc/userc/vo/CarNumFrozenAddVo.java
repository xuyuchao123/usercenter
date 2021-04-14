package com.xyc.userc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by 1 on 2021/4/13.
 */
@ApiModel(value="车牌号违章冻结新增请求参数对象类型")
public class CarNumFrozenAddVo
{
    @NotNull(message = "carNum不能为空")
    @ApiModelProperty(value="车牌号", required = true, dataType="String")
    private String carNum;

    @Override
    public String toString() {
        return "CarNumFrozenAddVo{" +
                "carNum='" + carNum + '\'' +
                '}';
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

}
