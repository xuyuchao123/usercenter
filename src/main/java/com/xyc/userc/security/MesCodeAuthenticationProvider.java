package com.xyc.userc.security;

import com.xyc.userc.entity.User;
import com.xyc.userc.service.MobileService;
import com.xyc.userc.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by 1 on 2020/7/7.
 * 短信验证码登陆鉴权 Provider，实现了AuthenticationProvider接口
 */
public class MesCodeAuthenticationProvider implements AuthenticationProvider
{
    private UserService userService;

    private MobileService mobileService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        MesCodeAuthenticationToken mesCodeAuthenticationToken = (MesCodeAuthenticationToken) authentication;
        String mobile = (String) mesCodeAuthenticationToken.getPrincipal();
        String mesCode = (String) mesCodeAuthenticationToken.getCredentials();
        User user = this.retrieveUser(mobile);
        checkMesCode(mesCode,user);

        // 验证成功后重新 new 一个有鉴权的 mesCodeAuthenticationToken 返回
        MesCodeAuthenticationToken mesCodeAuthenticationTokenResult = new MesCodeAuthenticationToken(user, mesCode, user.getAuthorities());
        mesCodeAuthenticationTokenResult.setDetails(mesCodeAuthenticationToken.getDetails());
        return mesCodeAuthenticationTokenResult;
    }

    private  User retrieveUser(String mobile) throws AuthenticationException
    {
        try
        {
            User loadedUser = this.getUserService().loadUserByMobile(mobile);
            if(loadedUser == null)
            {
                throw new InternalAuthenticationServiceException("UserService returned null, which is an interface contract violation");
            }
            else
            {
                return loadedUser;
            }
        }
        catch (MobileNotFoundException var4)
        {
            throw var4;
        }
        catch (InternalAuthenticationServiceException var5)
        {
            throw var5;
        }
        catch (Exception var6)
        {
            throw new InternalAuthenticationServiceException(var6.getMessage(), var6);
        }
    }


    private void checkMesCode(String mesCode, User user) throws AuthenticationException
    {
        if(mesCode == null || "".equals(mesCode))
        {
            throw new MesCodeNotFoundException("短信验证码不能为空");
        }
        try
        {
            String storedMesCode = this.getMobileService().loadMesCodeByMobile(user.getMobilePhone());
            if(storedMesCode == null)
            {
                throw new MesCodeExpiredException("短信验证码过期，请点击重新发送");
            }
            if(!storedMesCode.equals(mesCode))
            {
                throw new MesCodeErrorException("短信验证码错误");
            }
            else        //验证通过，将当前验证码改为无效状态
            {
                this.getMobileService().setMesCodeInvalid(user.getMobilePhone());
            }

        }
        catch (AuthenticationException e)
        {
            throw e;
        }
        catch (Exception var6)
        {
            throw new InternalAuthenticationServiceException(var6.getMessage(), var6);
        }
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return MesCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public MobileService getMobileService() {
        return mobileService;
    }

    public void setMobileService(MobileService mobileService) {
        this.mobileService = mobileService;
    }
}
