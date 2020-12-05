package com.xyc.userc.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by 1 on 2020/7/22.
 * 访问决策管理器，对当前请求的资源进行权限判断，判断当前登录用户是否拥有该权限，
 * 如果有就放行，如果没有就抛出一个"权限不足"的异常
 */
@Component
public class MyAccessDecisionManager implements AccessDecisionManager
{

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException
    {
        Iterator<ConfigAttribute> iterator = collection.iterator();
        //遍历可访问当前请求路径的所有权限，判断登录用户是否拥有这些权限（拥有其中一个权限即可访问）
        while (iterator.hasNext())
        {
            ConfigAttribute configAttribute = iterator.next();
            //当前请求的权限
            String needRoleId = configAttribute.getAttribute();
            //当前用户所的权限
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities)
            {
                if (authority.getAuthority().equals(needRoleId))
                {
                    return;
                }
            }
        }

        ((FilterInvocation)o).getResponse().setStatus(HttpStatus.FORBIDDEN.value());
        throw new AccessDeniedException("权限不足!");
//        throw new RuntimeException("权限不足!");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
