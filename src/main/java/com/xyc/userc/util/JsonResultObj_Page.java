package com.xyc.userc.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by 1 on 2020/10/12.
 */
@ApiModel(value="统一接口返回值类型带分页信息")
public class JsonResultObj_Page<T> extends JsonResultObj<T>
{
    @ApiModelProperty(value="返回数据的总数", example="100", position=4)
    private String total;
    @ApiModelProperty(value="当前返回的页数", example="1", position=5)
    private String page;
    @ApiModelProperty(value="每页显示的数据条数", example="10", position=6)
    private String size;

    //请求成功/失败并且无返回数据对象，resMsg都为默认值
    public JsonResultObj_Page(boolean isSuccess)
    {
        super(isSuccess);
    }

    //请求失败并返回失败原因
    public JsonResultObj_Page(boolean isSuccess,JsonResultEnum jsonResultEnum)
    {
        super(isSuccess,jsonResultEnum);
    }

    //请求失败并返回失败原因
    public JsonResultObj_Page(boolean isSuccess, Integer resCode, String msg)
    {
        super(isSuccess,resCode,msg);
    }

    //请求成功/失败并返回数据对象,resMsg为默认值
    public JsonResultObj_Page(boolean isSuccess, T resData, String total,String page, String size)
    {
        super(isSuccess,resData);
        this.total = total;
        this.page = page;
        this.size = size;
    }

    //请求成功/失败并返回数据对象
    public JsonResultObj_Page(boolean isSuccess, JsonResultEnum jsonResultEnum, T resData, String total, String page, String size)
    {
        super(isSuccess,jsonResultEnum,resData);
        this.total = total;
        this.page = page;
        this.size = size;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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
