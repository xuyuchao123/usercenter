package com.xyc.userc.vo;

import com.xyc.userc.entity.CarNumOpenId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 1 on 2021/4/8.
 */
@ApiModel(value="车牌号信息返回对象类型")
public class CarNumOpenIdVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="查询结果是否为空，返回true表示结果不为空，返回false表示结果为空", position=0)
    private boolean hasResult;

    @ApiModelProperty(value="查询结果列表", position=1)
    private List<CarNumOpenId> carNumOpenIds;

    public boolean isHasResult() {
        return hasResult;
    }

    public void setHasResult(boolean hasResult) {
        this.hasResult = hasResult;
    }

    public List<CarNumOpenId> getCarNumOpenIds() {
        return carNumOpenIds;
    }

    public void setCarNumOpenIds(List<CarNumOpenId> carNumOpenIds) {
        this.carNumOpenIds = carNumOpenIds;
    }
}
