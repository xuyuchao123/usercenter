package com.xyc.userc.controller;

import com.alibaba.fastjson.JSONObject;
import com.xyc.userc.entity.Application;
import com.xyc.userc.entity.User;
import com.xyc.userc.security.MesCodeErrorException;
import com.xyc.userc.security.MesCodeExpiredException;
import com.xyc.userc.service.ApplicationService;
import com.xyc.userc.service.UserService;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.JsonResultObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by 1 on 2020/7/2.
 * 用户操作控制器
 */

@Controller
@RequestMapping("/user")
@Api(tags = "系统用户管理相关api")
public class UserController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Resource
    UserService userService;


    @RequestMapping(value = "/getCurrentUser",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="获取当前用户信息")
    public JsonResultObj getCurrentUser()
    {
        LOGGER.debug("开始获取当前用户信息");
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println(user);
        JsonResultObj resultObj = new JsonResultObj(true,user);
        LOGGER.debug("结束获取当前用户信息");
        return resultObj;
    }


}
