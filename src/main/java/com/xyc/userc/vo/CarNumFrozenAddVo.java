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

    @NotNull(message = "expireDate不能为空")
    @ApiModelProperty(value="冻结到期日期", required = true, dataType="String")
    private Long expireDate;

    @Override
    public String toString() {
        return "CarNumFrozenAddVo{" +
                "carNum='" + carNum + '\'' +
                ", expireDate='" + expireDate + '\'' +
                '}';
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }
}
