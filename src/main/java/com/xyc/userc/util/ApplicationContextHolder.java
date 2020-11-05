package com.xyc.userc.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by 1 on 2020/11/5.
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware
{
    private static ApplicationContext ac;

    /**
     *ApplicationContextAware接口的方法，会自动将当前applicationContext对象注入进来
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        ac = applicationContext;
    }

    /**
     * 获取ApplicationContext
     */
    public static ApplicationContext getApplicationContext()
    {
        return ac;
    }

    /**
     * 根据bean名称获取一个bean
     */
    public static Object getBean( String beanName )
    {
        return ac.getBean(beanName);
    }

}
