package com.xyc.userc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1 on 2021/2/7.
 */
@ApiModel(value="大厅报道列表评论返回对象类型")
public class HallReportCommentVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="评论内容", position=0)
    private String comment;

    @ApiModelProperty(value="创建时间", position=1)
    private String gmtCreate;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
