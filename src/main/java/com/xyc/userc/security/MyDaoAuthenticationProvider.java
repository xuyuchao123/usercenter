package com.xyc.userc.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 1 on 2020/8/10.
 * 继承DaoAuthenticationProvider 重写additionalAuthenticationChecks 实现验证码功能
 */
public class MyDaoAuthenticationProvider extends DaoAuthenticationProvider
{
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException
    {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String verifyCode = req.getParameter("verifyCode");
        String verify_code = (String) req.getSession().getAttribute("verify_code");
        if (verifyCode == null || verify_code == null || !verifyCode.equals(verify_code))
        {
            throw new VerifyCodeErrorException("验证码错误");
        }
        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
