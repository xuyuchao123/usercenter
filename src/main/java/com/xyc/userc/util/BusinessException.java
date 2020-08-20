package com.xyc.userc.util;

/**
 * Created by 1 on 2020/8/19.
 */
public class BusinessException extends RuntimeException
{
    private JsonResultEnum jsonResultEnum;

    public BusinessException(String msg)
    {
        super(msg);
    }

    public BusinessException(JsonResultEnum jsonResultEnum)
    {
        super();
        this.jsonResultEnum = jsonResultEnum;
    }

    public JsonResultEnum getJsonResultEnum() {
        return jsonResultEnum;
    }

    public void setJsonResultEnum(JsonResultEnum jsonResultEnum) {
        this.jsonResultEnum = jsonResultEnum;
    }
}