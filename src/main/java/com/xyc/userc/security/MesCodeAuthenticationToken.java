package com.xyc.userc.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import javax.security.auth.Subject;
import java.util.Collection;

/**
 * Created by 1 on 2020/7/7.
 * 短信登录 AuthenticationToken
 */
public class MesCodeAuthenticationToken extends AbstractAuthenticationToken
{
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    //表示用户手机号
    private final Object principal;

    //表示用户输入的验证码
    private Object mesCode;


     //构建一个没有鉴权的 SmsCodeAuthenticationToken
    public MesCodeAuthenticationToken(Object principal,Object mesCode)
    {
        super(null);
        this.principal = principal;
        this.mesCode = mesCode;
        setAuthenticated(false);
    }

    //构建一个有鉴权的 SmsCodeAuthenticationToken
    public MesCodeAuthenticationToken(Object principal, Object mesCode, Collection<? extends GrantedAuthority> authorities)
    {
        super(authorities);
        this.principal = principal;
        this.mesCode = mesCode;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.mesCode;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }


}
