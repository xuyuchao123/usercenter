package com.xyc.userc.controller;

import com.alibaba.fastjson.JSON;
import com.xyc.userc.util.BusinessException;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.JsonResultObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 2019/6/29.
 * 处理controller层抛出的异常（service层抛出的异常由controller层捕获处理）
 */

@ControllerAdvice
public class ExceptionController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonResultObj handleException(Exception e, HttpServletRequest request)
    {
        if(e instanceof BindException)
        {
            List<FieldError> fieldErrors = ((BindException)e).getFieldErrors();
            StringBuilder sb = new StringBuilder();
            for (FieldError fieldError : fieldErrors)
            {
                sb.append(fieldError.getDefaultMessage()).append(", ");
            }
            LOGGER.error("系统错误：{}", sb.toString());
            return new JsonResultObj(false,JsonResultEnum.FAIL.getCode(),sb.toString());
        }
        if(e instanceof MissingServletRequestParameterException)
        {
            String message = e.getMessage();
            LOGGER.error("系统错误：{}", message);
            return new JsonResultObj(false,JsonResultEnum.FAIL.getCode(),message);
        }
        e.printStackTrace();
        LOGGER.error("系统错误：", e);
        JsonResultObj jsonResultObj = new JsonResultObj(false);
        return jsonResultObj;
    }
}
