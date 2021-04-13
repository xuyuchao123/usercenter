package com.xyc.userc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by 1 on 2021/4/13.
 */
@ApiModel(value="车牌号违章冻结信息查询请求参数对象类型")
public class CarNumFrozenQueryVo
{
    @ApiModelProperty(value="车牌号", required = false, dataType="String")
    private String carNum;

    @ApiModelProperty(value="冻结状态 1：已冻结 0：未冻结", required = false, dataType="String")
    private String frozenStatus;

    @NotNull(message = "page不能为空")
    @ApiModelProperty(value="当前页码", required = true, dataType="String")
    private String page;

    @NotNull(message = "size不能为空")
    @ApiModelProperty(value="每页记录条数", required = true, dataType="String")
    private String size;

    @Override
    public String toString() {
        return "CarNumFrozenQueryVo{" +
                "carNum='" + carNum + '\'' +
                ", frozenStatus='" + frozenStatus + '\'' +
                ", page='" + page + '\'' +
                ", size='" + size + '\'' +
                '}';
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getFrozenStatus() {
        return frozenStatus;
    }

    public void setFrozenStatus(String frozenStatus) {
        this.frozenStatus = frozenStatus;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
