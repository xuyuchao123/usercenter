package com.xyc.userc.security;

import com.alibaba.fastjson.JSON;
import com.xyc.userc.dao.UserMapper;
import com.xyc.userc.entity.User;
import com.xyc.userc.util.JsonResultObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by 1 on 2020/6/30.
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
    @Autowired
    private UserMapper userMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authentication) throws IOException, ServletException
    {
        //更新用户表上次登录时间、更新人、更新时间等字段
        User user = (User) authentication.getPrincipal();
        user.setLastLoginTime(new Date());
        userMapper.updateByPrimaryKey(user);
        //也进行一些处理，比如登录成功之后将用户的菜单权限返回给前台
        //将用户对象转为json数据返回
        JsonResultObj jsonResultObj = new JsonResultObj(true,user);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(jsonResultObj));
    }
}
