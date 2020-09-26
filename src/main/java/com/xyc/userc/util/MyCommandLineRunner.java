package com.xyc.userc.util;

import com.xyc.userc.controller.TemplateController;
import com.xyc.userc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by 1 on 2020/9/26.
 * 项目启动时执行，将绑定的所有用户信息存入redis
 */
@Component
@Order
public class MyCommandLineRunner implements CommandLineRunner
{

    protected static final Logger LOGGER = LoggerFactory.getLogger(MyCommandLineRunner.class);

    @Resource
    UserService userService;

    //启动s任务的核心逻辑，当项目启动时，run方法会被自动执行。
    @Override
    public void run(String... args) throws Exception
    {
        LOGGER.info("开始存入用户信息至redis");
        userService.getUserInfoVo();
        LOGGER.info("结束存入用户信息至redis");
    }

}
