package com.xyc.userc.security;

import com.xyc.userc.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * Created by 1 on 2020/7/17.
 * 根据请求路径从数据库中查出当前资源路径需要哪些权限才能访问
 */
@Component
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource
{
    @Autowired
    RoleService roleService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws RuntimeException
    {
        //获取请求地址
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        //查询该请求需要的权限id列表
        String[] attributes = null;
        try
        {
            List<Long> roleIdList = roleService.queryIdListByPath(requestUrl);
            if (roleIdList == null || roleIdList.size() == 0)
            {
                //请求路径没有配置权限，表明该请求接口可以任意访问
                return null;
            }
            attributes = new String[roleIdList.size()];
            for (int i = 0; i < roleIdList.size(); i++)
            {
                attributes[i] = String.valueOf(roleIdList.get(i));
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return SecurityConfig.createList(attributes);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
