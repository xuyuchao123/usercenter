package com.xyc.userc.security;

import com.alibaba.fastjson.JSON;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.JsonResultObj;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 1 on 2020/7/1.
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler
{

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException
    {
        System.out.println(e.getMessage());
        JsonResultEnum jsonResultEnum = null;
        if (e instanceof UsernameNotFoundException)            //用户不存在
        {
            jsonResultEnum = JsonResultEnum.USER_ACCOUNT_NOT_EXIST;
        }
        else if(e instanceof MobileNotFoundException)
        {
            jsonResultEnum = JsonResultEnum.USER_MOBILE_NOT_EXIST;      //手机号未注册
        }
        else if(e instanceof MesCodeExpiredException)
        {
            jsonResultEnum = JsonResultEnum.USER_MESCODE_EXPIRED;      //短信验证码过期
        }
        else if(e instanceof MesCodeNotFoundException)
        {
            jsonResultEnum = JsonResultEnum.USER_MESCODE_NOT_EXIST;      //短信验证码为空
        }
        else if(e instanceof MesCodeErrorException)
        {
            jsonResultEnum = JsonResultEnum.USER_MESCODE_ERROR;      //短信验证码错误
        }
        else if(e instanceof BadCredentialsException)                //密码错误
        {
            jsonResultEnum = JsonResultEnum.USER_PASSWORD_ERROR;
        }
        else if(e instanceof DisabledException)                      //账号不可用
        {
            jsonResultEnum = JsonResultEnum.USER_ACCOUNT_DISABLE;
        }
        else if(e instanceof LockedException)                              //账号锁定
        {
            jsonResultEnum = JsonResultEnum.USER_ACCOUNT_LOCKED;
        }
        else
        {
            jsonResultEnum = JsonResultEnum.FAIL;
        }
        JsonResultObj jsonResultObj = new JsonResultObj(false,jsonResultEnum);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(jsonResultObj));
    }
}
