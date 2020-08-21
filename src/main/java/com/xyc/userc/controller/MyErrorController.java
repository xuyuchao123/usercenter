package com.xyc.userc.controller;

import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.JsonResultObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by 1 on 2020/7/23.
 * 模仿springboot默认的 basicErrorController，处理controller层之前的系统异常
 * controller层及之后的异常由ExceptionController处理
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
@ApiIgnore
public class MyErrorController extends AbstractErrorController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(MyErrorController.class);

    private final ErrorProperties errorProperties;

    @Autowired
    public MyErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties)
    {
        super(errorAttributes);
        this.errorProperties=serverProperties.getError();
    }

    @Override
    public String getErrorPath()
    {
        return errorProperties.getPath();
    }

    @RequestMapping
    @ResponseBody
    public JsonResultObj error(HttpServletRequest request)
    {
        JsonResultObj jsonResultObj;
        Map<String, Object> body = this.getErrorAttributes(request, this.isIncludeStackTrace(request, MediaType.ALL));
        HttpStatus status = this.getStatus(request);
        LOGGER.error("系统出错！status={} errorMsg:{}",status,body.get("message"));
//        if(status == HttpStatus.FORBIDDEN)
//        {
//            LOGGER.error("权限不足! 访问路径：{}",body.get("path"));
//            jsonResultObj = new JsonResultObj(false, JsonResultEnum.NO_PERMISSION);
//        }
//        else
//        {
//            jsonResultObj = new JsonResultObj(false);
//        }
        jsonResultObj = new JsonResultObj(false);
        return jsonResultObj;
    }

    protected boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces)
    {
        ErrorProperties.IncludeStacktrace include = this.getErrorProperties().getIncludeStacktrace();
        return include == ErrorProperties.IncludeStacktrace.ALWAYS?
                true:(include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM?this.getTraceParameter(request):false);
    }

    private ErrorProperties getErrorProperties()
    {
        return this.errorProperties;
    }

}
