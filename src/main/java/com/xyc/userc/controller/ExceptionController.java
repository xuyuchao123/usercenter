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
//        return JSON.toJSONString(jsonResultObj);
//        String xr = request.getHeader("X-Requested-With");
//        if(xr != null && "XMLHttpRequest".equalsIgnoreCase(xr))              //ajax请求
//        {
//            LOGGER.error("系统错误：", e);
//            JsonResultObj jsonResultObj = new JsonResultObj(false);
//            return JSON.toJSONString(jsonResultObj);
//        }
//        else                        //普通请求
//        {
//            LOGGER.error("系统错误：", e);
//            String accept = request.getHeader("Accept");
//            if(accept != null && accept.contains("application/json"))
//            {
//                Map jsonMap = new HashMap<String, Object>();
//                jsonMap.put("errMsg", "系统错误,请联系管理员！");
//                return jsonMap;
//            }
//            return "500";
//        }
    }
}
