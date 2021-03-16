package com.xyc.userc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by 1 on 2021/3/16.
 */
@ApiModel(value="大厅报道及打印队列返回对象类型")
public class HallReportPrintQueueVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="车牌号", position=0)
    private String carNum;

    @ApiModelProperty(value="队列编号", position=1)
    private String no;

    @ApiModelProperty(value="超时时间", position=2)
    private Long timeout;

    @ApiModelProperty(value="报道时间", position=3)
    private Long reportTime;

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public Long getReportTime() {
        return reportTime;
    }

    public void setReportTime(Long reportTime) {
        this.reportTime = reportTime;
    }
}
