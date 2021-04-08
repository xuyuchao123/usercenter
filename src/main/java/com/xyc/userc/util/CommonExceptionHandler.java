package com.xyc.userc.util;

import org.slf4j.Logger;

/**
 * Created by 1 on 2020/8/25.
 * 通用业务异常处理操作
 */

public class CommonExceptionHandler
{
    public static JsonResultObj handException(Exception e, String msg, Logger LOGGER)
    {
        if(e instanceof BusinessException)
        {
            if(((BusinessException)e).getJsonResultEnum() != null)
            {
                LOGGER.error(msg + "：{}", ((BusinessException)e).getJsonResultEnum().getMessage());
                return new JsonResultObj(false,((BusinessException)e).getJsonResultEnum());
            }
            else
            {
                LOGGER.error(msg + "：{}", e.getMessage());
                return new JsonResultObj(false, JsonResultEnum.FAIL.getCode(), e.getMessage());
            }

        }
        else
        {
            e.printStackTrace();
            LOGGER.error(msg + "：{}",e);
            return new JsonResultObj(false);
        }
    }

    public static JsonResultObj_Page handException_page(Exception e, String msg, Logger LOGGER)
    {
        if(e instanceof BusinessException)
        {
            if(((BusinessException)e).getJsonResultEnum() != null)
            {
                LOGGER.error(msg + "：{}", ((BusinessException)e).getJsonResultEnum().getMessage());
                return new JsonResultObj_Page(false,((BusinessException)e).getJsonResultEnum());
            }
            else
            {
                LOGGER.error(msg + "：{}", e.getMessage());
                return new JsonResultObj_Page(false, JsonResultEnum.FAIL.getCode(), e.getMessage());
            }

        }
        else
        {
            e.printStackTrace();
            LOGGER.error(msg + "：{}",e);
            return new JsonResultObj_Page(false);
        }
    }
}
