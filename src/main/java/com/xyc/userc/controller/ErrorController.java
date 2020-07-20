package com.xyc.userc.controller;

import com.xyc.userc.util.JsonResultObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by 1 on 2020/7/17.
 * 处理controller层之前的系统异常，springboot默认跳转到"/error",前后端分离模式下
 * 不配置error页面，而是返回出错提示信息
 */
@Controller
@ApiIgnore
public class ErrorController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    @RequestMapping(value = "/error",method = RequestMethod.POST)
    @ResponseBody
    public JsonResultObj errorHandler()
    {
        LOGGER.error("系统出错！");
        JsonResultObj jsonResultObj = new JsonResultObj(false);
        return jsonResultObj;
    }
}
