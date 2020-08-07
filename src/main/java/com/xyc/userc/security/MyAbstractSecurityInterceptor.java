package com.xyc.userc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by 1 on 2020/7/22.
 * 权限拦截器，添加到springsecurity的拦截器链中，拦截请求，使用MyFilterInvocationSecurityMetadataSource
 * 获取请求所需的权限信息，再使用MyAccessDecisionManager判断登录用户是否拥有当前请求所需的权限
 */
//@Service
public class MyAbstractSecurityInterceptor extends AbstractSecurityInterceptor implements Filter
{

//    @Autowired
    private FilterInvocationSecurityMetadataSource securityMetadataSource;

//    private MyAccessDecisionManager myAccessDecisionManager;

//    @Autowired
    public void setMyAccessDecisionManager(MyAccessDecisionManager myAccessDecisionManager)
    {
        super.setAccessDecisionManager(myAccessDecisionManager);
    }

    @Override
    public Class<?> getSecureObjectClass()
    {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource()
    {
        return this.securityMetadataSource;
    }


    public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource securityMetadataSource)
    {
        this.securityMetadataSource = securityMetadataSource;
    }



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try
        {
            //执行下一个拦截器
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        }
        finally
        {
            super.afterInvocation(token, null);
        }
    }

}
