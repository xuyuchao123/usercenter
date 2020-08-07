package com.xyc.userc.security;

import com.alibaba.fastjson.JSON;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.JsonResultObj;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 1 on 2020/6/28.
 * 用于处理用户未登录的异常
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint
{
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException
    {
        System.out.println(e.getMessage());
        JsonResultObj jsonResultObj = new JsonResultObj(false, JsonResultEnum.USER_NOT_LOGIN);
        response.setContentType("text/json;charset=utf-8");
        String json = JSON.toJSONString(jsonResultObj);
        response.getWriter().write(JSON.toJSONString(jsonResultObj));
    }
}
