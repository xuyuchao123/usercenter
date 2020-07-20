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

    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="注册用户")
    @ApiImplicitParams({@ApiImplicitParam(name="username", value="用户名", required=true, dataType="String"),
            @ApiImplicitParam(name="password", value="密码", required=true, dataType="String"),
            @ApiImplicitParam(name="mobile", value="手机号", required=true, dataType="String"),
            @ApiImplicitParam(name="mesCode", value="短信验证码", required=true, dataType="String"),
            })

    public JsonResultObj addUser(String username, String password, String mobile, String mesCode)
    {
        LOGGER.debug("开始新增用户");
        JsonResultObj resultObj = null;
        try
        {
            byte isDelete = 0;
            byte isEnable = 1;
            byte isLocked = 0;
            userService.addUser(username, password, mobile, "", isDelete,
                    isEnable, isLocked, 1L,mesCode);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            LOGGER.error("新增用户失败：{}",e.getMessage());
            if(e instanceof MesCodeExpiredException)
            {
                resultObj = new JsonResultObj(false, JsonResultEnum.USER_MESCODE_EXPIRED);
            }
            else if(e instanceof MesCodeErrorException)
            {
                resultObj = new JsonResultObj(false, JsonResultEnum.USER_MESCODE_ERROR);
            }
            else
            {
                e.printStackTrace();
                resultObj = new JsonResultObj(false);
            }
        }
        LOGGER.debug("结束新增用户");
        return resultObj;
    }

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
