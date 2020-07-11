package com.xyc.userc.controller;

import com.alibaba.fastjson.JSONObject;
import com.xyc.userc.entity.Application;
import com.xyc.userc.entity.User;
import com.xyc.userc.service.ApplicationService;
import com.xyc.userc.service.UserService;
import com.xyc.userc.util.JsonResultObj;
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
public class UserController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Resource
    UserService userService;

    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    @ResponseBody
    public Object addUser(String username, String password, String userRealName, String userCreate)
    {
        LOGGER.debug("开始新增用户");
        Object jsonObj = null;
        try
        {
            byte isDelete = 0;
            byte isEnable = 1;
            byte isLocked = 0;
            userService.addUser( username, password, userRealName, isDelete,
                    isEnable, isLocked, Long.valueOf(userCreate));
            JsonResultObj resultObj = new JsonResultObj(true);
            jsonObj = JSONObject.toJSON(resultObj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("新增用户失败：{}",e.getMessage());
            JsonResultObj resultObj = new JsonResultObj(false);
            jsonObj = JSONObject.toJSON(resultObj);
        }
        LOGGER.debug("结束新增用户");
        return jsonObj;
    }

    @RequestMapping(value = "/getCurrentUser",method = RequestMethod.GET)
    @ResponseBody
    public Object getCurrentUser(HttpSession session)
    {
        LOGGER.debug("开始获取当前用户信息");
//        session.getAttribute("")
        Object jsonObj = null;
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println(user);
        JsonResultObj resultObj = new JsonResultObj(true,user);
        jsonObj = JSONObject.toJSON(resultObj);
        LOGGER.debug("结束获取当前用户信息");
        return jsonObj;
    }




}
