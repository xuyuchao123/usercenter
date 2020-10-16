package org.springframework.security.web.authentication;

/**
 * Created by 1 on 2020/7/7.
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xyc.userc.security.MesCodeAuthenticationToken;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
    //form表单中手机号码的输入框的name属性
    public static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";

    //form表单中验证码的输入框的name属性
    public static final String SPRING_SECURITY_FORM_MESCODE_KEY = "mesCode";
    private String usernameParameter = "username";
    private String passwordParameter = "password";
    private String loginTypeParameter = "type";
    private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;
    private String mesCodeParameter = SPRING_SECURITY_FORM_MESCODE_KEY;

    private boolean postOnly = true;

    public UsernamePasswordAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if(this.postOnly && !request.getMethod().equals("POST"))
        {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        else
        {
            System.out.println("************************************************session=" + request.getSession());
            System.out.println("************************************************sessionId=" + request.getSession().getId());
            String loginType = this.obtainLoginType(request);
            if(loginType.equals("account"))
            {
                String username = this.obtainUsername(request);
                String password = this.obtainPassword(request);
                if(username == null)
                {
                    username = "";
                }

                if(password == null)
                {
                    password = "";
                }

                username = username.trim();
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
                this.setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
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
                this.setDetails_mes(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            }

        }
    }

    @Nullable
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(this.passwordParameter);
    }

    @Nullable
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(this.usernameParameter);
    }

    @Nullable
    protected String obtainLoginType(HttpServletRequest request) {
        return request.getParameter(this.loginTypeParameter);
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

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    protected void setDetails_mes(HttpServletRequest request, MesCodeAuthenticationToken authRequest)
    {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setUsernameParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
        this.usernameParameter = usernameParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
        this.passwordParameter = passwordParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getUsernameParameter() {
        return this.usernameParameter;
    }

    public final String getPasswordParameter() {
        return this.passwordParameter;
    }
}

