package com.xyc.userc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 1 on 2021/3/19.
 */
@ApiModel(value="大厅报道动态二维码字符串返回对象类型")
public class QRCodeStrVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="二维码字符串", position=0)
    private List<String> qRCode;

    @ApiModelProperty(value="下次更新时间戳", position=1)
    private Long nextTick;

    public List<String> getqRCode() {
        return qRCode;
    }

    public void setqRCode(List<String> qRCode) {
        this.qRCode = qRCode;
    }

    public Long getNextTick() {
        return nextTick;
    }

    public void setNextTick(Long nextTick) {
        this.nextTick = nextTick;
    }
}
