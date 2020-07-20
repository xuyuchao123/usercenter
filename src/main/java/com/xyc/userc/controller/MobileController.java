package com.xyc.userc.controller;

import com.alibaba.fastjson.JSONObject;
import com.xyc.userc.security.MesCodeErrorException;
import com.xyc.userc.security.MesCodeExpiredException;
import com.xyc.userc.service.MobileService;
import com.xyc.userc.service.UserService;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.JsonResultObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import smsservice.ISMS;
import smsservice.SMSService;

import javax.annotation.Resource;
import java.util.Random;

/**
 * Created by 1 on 2020/7/2.
 * 手机验证码操作控制器
 */

@Controller
@CrossOrigin
@RequestMapping("/mes")
@Api(tags = "手机登录/注册相关api")
public class MobileController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(MobileController.class);

    @Resource
    MobileService mobileService;

    @Resource
    UserService userService;

    //发送验证码
    @RequestMapping(value = "/sendMesCode",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="发送短信验证码")
    @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType =  "String")
    public JsonResultObj sendMesCode(String mobile)
    {
        LOGGER.debug("开始生成发送短信验证码 mobile={}",mobile);
        JsonResultObj resultObj = null;
        try
        {
            //检查手机号是否存在,mobileExist=1表示存在
            int mobileExist =  userService.checkUserExistByMobile(mobile);
            if(mobileExist == 1)
            {
                String mesCode = null;
                //检查是否存在有效的短信验证码
                mesCode = mobileService.loadMesCodeByMobile(mobile);
                if(mesCode == null)
                {
                    //不存在有效验证码就随机生成一个6位数短信验证码
                    mesCode = String.valueOf(new Random().nextInt(899999) + 100000);
                    //将新生成的验证码存入数据库
                    mobileService.addMesCode(mobile, mesCode);
                }
                LOGGER.debug("成功生成短信验证码 mesCode={}",mesCode);
                //发送验证码短信功能。。。
                ISMS smsService = new SMSService().getSMSImplPort();
                smsService.smsSend(mobile,mesCode);
                resultObj = new JsonResultObj(true);
            }
            else
            {
                LOGGER.error("短信验证码生成发送失败，手机号不存在 mobile={}",mobile);
                resultObj = new JsonResultObj(false, JsonResultEnum.USER_MOBILE_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("短信验证码生成发送失败：{}",e.getMessage());
            resultObj = new JsonResultObj(false);
        }
        LOGGER.debug("结束生成发送短信验证码");
        return resultObj;
    }

    //重置密码
    @RequestMapping(value = "/resetPassword",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="重置密码")
    @ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, dataType = "String")})
    public JsonResultObj resetPassword(String mobile, String newPassword)
    {
        LOGGER.debug("开始重置密码 mobile={} newPassword={}",mobile,newPassword);
        JsonResultObj resultObj = null;
        try
        {
            userService.updatePassword(mobile,newPassword);
            resultObj = new JsonResultObj(true);
            LOGGER.debug("重置密码成功 mobile={} newPassword={}",mobile,newPassword);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            LOGGER.error("重置密码失败：",e.getMessage());
            resultObj = new JsonResultObj(false);
        }
        LOGGER.debug("结束重置密码 mobile={} newPassword={}",mobile,newPassword);
        return resultObj;
    }

    //测试用户名是否已注册
    @RequestMapping(value = "/checkUsernameExist",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="测试用户名是否已注册")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    public JsonResultObj checkUsernameExist(String username)
    {
        LOGGER.debug("开始检查用户名是否已注册 username={}",username);
        JsonResultObj resultObj = null;
        try
        {
            int usernameExist = userService.checkUserRegByUsername(username);
            if(usernameExist == 0)
            {
                LOGGER.debug("用户名未注册 username={}",username);
                resultObj = new JsonResultObj(true);
            }
            else
            {
                LOGGER.debug("用户名已注册 username={}",username);
                resultObj = new JsonResultObj(false,JsonResultEnum.USER_ACCOUNT_EXIST);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            LOGGER.error("检查用户名是否已注册失败：",e.getMessage());
            resultObj = new JsonResultObj(false);
        }
        LOGGER.debug("结束检查用户名是否已注册 username={}",username);
        return resultObj;

    }

    //测试手机号是否已注册
    @RequestMapping(value = "/checkMobileExist",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="测试手机号是否已注册")
    @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String")
    public JsonResultObj checkMobileExist(String mobile)
    {
        LOGGER.debug("开始检查手机号是否已注册 mobile={}",mobile);
        JsonResultObj resultObj = null;
        try
        {
            int mobileExist = userService.checkUserRegByMobile(mobile);
            if(mobileExist == 0)
            {
                LOGGER.debug("手机号未注册 mobile={}",mobile);
                resultObj = new JsonResultObj(true);
            }
            else
            {
                LOGGER.debug("手机号已注册 mobile={}",mobile);
                resultObj = new JsonResultObj(false,JsonResultEnum.USER_MOBILE_EXIST);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            LOGGER.error("检查手机号是否已注册失败：",e.getMessage());
            resultObj = new JsonResultObj(false);
        }
        LOGGER.debug("结束检查用户名是否已注册 mobile={}",mobile);
        return resultObj;
    }


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


}
