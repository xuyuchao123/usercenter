package com.xyc.userc.security;

import com.alibaba.fastjson.JSON;
import com.xyc.userc.util.JsonResultObj;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 1 on 2020/7/15.
 * 退出登录成功处理逻辑
 */
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler
{
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
        JsonResultObj jsonResultObj = new JsonResultObj(true);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(jsonResultObj));
    }
}
