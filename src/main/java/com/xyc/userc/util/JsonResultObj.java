package com.xyc.userc.util;

import com.github.pagehelper.Page;

import java.io.Serializable;

/**
 * Created by 1 on 2020/6/29.
 * 统一json返回结果实体
 */
public class JsonResultObj<T> implements Serializable
{
    private Boolean isSuccess;
    private Integer resCode;
    private String resMsg;
    private T resData;

    public JsonResultObj()
    {

    }

    //请求成功/失败并且无返回数据对象，resMsg都为默认值
    public JsonResultObj(boolean isSuccess)
    {
        this.isSuccess = isSuccess;
        if(isSuccess)
        {
            this.resCode = JsonResultEnum.SUCCESS.getCode();
            this.resMsg = JsonResultEnum.SUCCESS.getMessage();
        }
        else
        {
            this.resCode = JsonResultEnum.FAIL.getCode();
            this.resMsg = JsonResultEnum.FAIL.getMessage();
        }
    }

    //请求失败并返回失败原因
    public JsonResultObj(boolean isSuccess,JsonResultEnum jsonResultEnum)
    {
        this.isSuccess = isSuccess;
        this.resCode = jsonResultEnum.getCode();
        this.resMsg = jsonResultEnum.getMessage();
    }

    //请求成功/失败并返回数据对象,resMsg为默认值
    public JsonResultObj(boolean isSuccess, T resData)
    {
        this.isSuccess = isSuccess;
        this.resData = resData;
        if(isSuccess)
        {
            this.resCode = JsonResultEnum.SUCCESS.getCode();
            this.resMsg = JsonResultEnum.SUCCESS.getMessage();
        }
        else
        {
            this.resCode = JsonResultEnum.FAIL.getCode();
            this.resMsg = JsonResultEnum.FAIL.getMessage();
        }
    }

    //请求成功/失败并返回数据对象
    public JsonResultObj(boolean isSuccess, JsonResultEnum jsonResultEnum, T resData)
    {
        this.isSuccess = isSuccess;
        this.resData = resData;
        this.resCode = jsonResultEnum.getCode();
        this.resMsg = jsonResultEnum.getMessage();
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Integer getResCode() {
        return resCode;
    }

    public void setResCode(Integer resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public T getResData() {
        return resData;
    }

    public void setResData(T resData) {
        this.resData = resData;
    }
}
