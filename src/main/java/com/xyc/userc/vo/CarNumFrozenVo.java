package com.xyc.userc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by 1 on 2021/4/10.
 */
@ApiModel(value="车牌号违章冻结信息返回对象类型")
public class CarNumFrozenVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="车牌号", position=0)
    private String carNum;

    @ApiModelProperty(value="冻结状态 1：已冻结 0：未冻结", position=1)
    private Integer frozenStatus;

    @ApiModelProperty(value="冻结起始日期", position=2)
    private Long startDate;

    @ApiModelProperty(value="冻结到期日期", position=3)
    private Long expireDate;

//    @ApiModelProperty(value="创建时间", position=4)
//    private Long gmtCreate;
//
//    @ApiModelProperty(value="修改时间", position=5)
//    private Long gmtModified;

    @ApiModelProperty(value="累计违章次数", position=4)
    private Integer violationTimes;

    @ApiModelProperty(value="有无未处理违章 1：有未处理违章 0：无未处理违章", position=5)
    private Integer violationHndStatus;

    @ApiModelProperty(value="最近一次违章时间", position=6)
    private Long lastViolationTime;

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public Integer getFrozenStatus() {
        return frozenStatus;
    }

    public void setFrozenStatus(Integer frozenStatus) {
        this.frozenStatus = frozenStatus;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }

    public Integer getViolationTimes() {
        return violationTimes;
    }

    public void setViolationTimes(Integer violationTimes) {
        this.violationTimes = violationTimes;
    }

    public Integer getViolationHndStatus() {
        return violationHndStatus;
    }

    public void setViolationHndStatus(Integer violationHndStatus) {
        this.violationHndStatus = violationHndStatus;
    }

    public Long getLastViolationTime() {
        return lastViolationTime;
    }

    public void setLastViolationTime(Long lastViolationTime) {
        this.lastViolationTime = lastViolationTime;
    }
}
