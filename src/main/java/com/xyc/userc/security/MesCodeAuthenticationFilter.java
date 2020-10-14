package com.xyc.userc.security;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 1 on 2020/7/7.
 * 短信登录的鉴权过滤器
 * 该类暂时不用
 */
public class MesCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter
{
    //form表单中手机号码的输入框的name属性
    public static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";

    //form表单中验证码的输入框的name属性
    public static final String SPRING_SECURITY_FORM_MESCODE_KEY = "mesCode";


    private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;
    private String mesCodeParameter = SPRING_SECURITY_FORM_MESCODE_KEY;

    //是否仅POST方式
    private boolean postOnly = true;

    public MesCodeAuthenticationFilter()
    {
        // 短信验证码登录的post请求路径
        super(new AntPathRequestMatcher("/mes/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException
    {
        if(this.postOnly && !request.getMethod().equals("POST"))
        {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        else
        {
            String mobile = this.obtainMobile(request);
            String mesCode = this.obtainMesCode(request);
            if(mobile == null)
            {
                mobile = "";
            }
            if(mesCode == null)
            {
                mesCode = "";
            }
            mobile = mobile.trim();
            MesCodeAuthenticationToken authRequest = new MesCodeAuthenticationToken(mobile,mesCode);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    @Nullable
    protected String obtainMobile(HttpServletRequest request)
    {
        return request.getParameter(this.mobileParameter);
    }

    @Nullable
    protected String obtainMesCode(HttpServletRequest request)
    {
        return request.getParameter(this.mesCodeParameter);
    }

    protected void setDetails(HttpServletRequest request, MesCodeAuthenticationToken authRequest)
    {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setMobileParameter(String mobileParameter)
    {
        Assert.hasText(mobileParameter, "Mobile parameter must not be empty or null");
        this.mobileParameter = mobileParameter;
    }

    public void setMesCodeParameter(String mesCodeParameter)
    {
        Assert.hasText(mesCodeParameter, "message code parameter must not be empty or null");
        this.mesCodeParameter = mesCodeParameter;
    }

    public void setPostOnly(boolean postOnly)
    {
        this.postOnly = postOnly;
    }

    public final String getMobileParameter()
    {
        return this.mobileParameter;
    }

    public final String getMesCodeparameter()
    {
        return this.mesCodeParameter;
    }

}
