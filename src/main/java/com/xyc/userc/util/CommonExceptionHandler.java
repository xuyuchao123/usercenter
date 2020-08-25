package com.xyc.userc.util;

import org.slf4j.Logger;

/**
 * Created by 1 on 2020/8/25.
 * 通用业务异常处理操作
 */

public class CommonExceptionHandler
{
    public static JsonResultObj handException(Exception e, String msg, Logger LOGGER, JsonResultObj resultObj)
    {
        if(e instanceof BusinessException)
        {
            LOGGER.error(msg + "：{}", ((BusinessException)e).getJsonResultEnum().getMessage());
            resultObj = new JsonResultObj(false,((BusinessException)e).getJsonResultEnum());
        }
        else
        {
            e.printStackTrace();
            LOGGER.error(msg + "：{}",e.getMessage());
            resultObj = new JsonResultObj(false);
        }
        return resultObj;
    }
}
