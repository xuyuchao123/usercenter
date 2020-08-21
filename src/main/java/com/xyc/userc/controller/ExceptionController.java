package com.xyc.userc.controller;

import com.alibaba.fastjson.JSON;
import com.xyc.userc.util.JsonResultObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
        e.printStackTrace();
        LOGGER.error("系统错误：", e);
        JsonResultObj jsonResultObj = new JsonResultObj(false);
        return jsonResultObj;
    }
}
