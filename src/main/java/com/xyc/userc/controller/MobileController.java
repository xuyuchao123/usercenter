package com.xyc.userc.controller;

import com.alibaba.fastjson.JSONObject;
import com.xyc.userc.security.MesCodeErrorException;
import com.xyc.userc.security.MesCodeExpiredException;
import com.xyc.userc.service.MobileService;
import com.xyc.userc.service.UserService;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.JsonResultObj;
import io.swagger.annotations.*;
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
    @ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType =  "String"),
            @ApiImplicitParam(name = "type", value = "操作类型", required = true, dataType =  "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：发送成功 isSuccess=false：发送失败，resMsg为错误信息")})
    public JsonResultObj sendMesCode(String mobile, String type)
    {
        LOGGER.info("开始生成发送短信验证码 mobile={},type={}",mobile,type);
        JsonResultObj resultObj = null;
        try
        {
            if(!"register".equals(type))
            {
                //检查手机号是否存在,mobileExist=1表示存在
                int mobileExist =  userService.checkUserExistByMobile(mobile);
                if(mobileExist != 1)
                {
                    LOGGER.error("短信验证码生成发送失败，手机号不存在 mobile={}",mobile);
                    resultObj = new JsonResultObj(false, JsonResultEnum.USER_MOBILE_NOT_EXIST);
                    return resultObj;
                }
            }

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
            LOGGER.info("成功生成短信验证码 mesCode={}",mesCode);
            //发送验证码短信功能。。。
            ISMS smsService = new SMSService().getSMSImplPort();
            smsService.smsSend(mobile,mesCode);
            resultObj = new JsonResultObj(true);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("短信验证码生成发送失败：{}",e.getMessage());
            resultObj = new JsonResultObj(false);
        }
        LOGGER.info("结束生成发送短信验证码");
        return resultObj;
    }

    //重置密码
    @RequestMapping(value = "/resetPassword",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="重置密码")
    @ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, dataType = "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：重置密码成功 isSuccess=false：重置密码失败，resMsg为错误信息")})
    public JsonResultObj resetPassword(String mobile, String newPassword)
    {
        LOGGER.info("开始重置密码 mobile={} newPassword={}",mobile,newPassword);
        JsonResultObj resultObj = null;
        try
        {
            userService.updatePassword(mobile,newPassword);
            resultObj = new JsonResultObj(true);
            LOGGER.info("重置密码成功 mobile={} newPassword={}",mobile,newPassword);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            LOGGER.error("重置密码失败：",e.getMessage());
            resultObj = new JsonResultObj(false);
        }
        LOGGER.info("结束重置密码 mobile={} newPassword={}",mobile,newPassword);
        return resultObj;
    }

    //测试用户名是否已注册
    @RequestMapping(value = "/checkUsernameExist",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="测试用户名是否已注册")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：用户名未被注册 isSuccess=false：操作失败或用户名已被注册，resMsg为错误信息")})
    public JsonResultObj checkUsernameExist(String username)
    {
        LOGGER.info("开始检查用户名是否已注册 username={}",username);
        JsonResultObj resultObj = null;
        try
        {
            int usernameExist = userService.checkUserRegByUsername(username);
            if(usernameExist == 0)
            {
                LOGGER.info("用户名未注册 username={}",username);
                resultObj = new JsonResultObj(true);
            }
            else
            {
                LOGGER.info("用户名已注册 username={}",username);
                resultObj = new JsonResultObj(false,JsonResultEnum.USER_ACCOUNT_EXIST);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            LOGGER.error("检查用户名是否已注册失败：",e.getMessage());
            resultObj = new JsonResultObj(false);
        }
        LOGGER.info("结束检查用户名是否已注册 username={}",username);
        return resultObj;

    }

    //测试手机号是否已注册
    @RequestMapping(value = "/checkMobileExist",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="测试手机号是否已注册")
    @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String")
    @ApiResponses({@ApiResponse(code = 200, message = "isSuccess=true：手机号未被注册 isSuccess=false：操作失败或手机号已被注册，resMsg为错误信息")})
    public JsonResultObj checkMobileExist(String mobile)
    {
        LOGGER.info("开始检查手机号是否已注册 mobile={}",mobile);
        JsonResultObj resultObj = null;
        try
        {
            int mobileExist = userService.checkUserRegByMobile(mobile);
            if(mobileExist == 0)
            {
                LOGGER.info("手机号未注册 mobile={}",mobile);
                resultObj = new JsonResultObj(true);
            }
            else
            {
                LOGGER.info("手机号已注册 mobile={}",mobile);
                resultObj = new JsonResultObj(false,JsonResultEnum.USER_MOBILE_EXIST);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            LOGGER.error("检查手机号是否已注册失败：",e.getMessage());
            resultObj = new JsonResultObj(false);
        }
        LOGGER.info("结束检查用户名是否已注册 mobile={}",mobile);
        return resultObj;
    }

    //校验短信验证码是否正确
    @RequestMapping(value = "/checkMesCode",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="校验短信验证码是否正确")
    @ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "mesCode", value = "验证码", required = true, dataType = "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：验证码正确 isSuccess=false：操作失败或验证码过期/错误，resMsg为错误信息")})
    public JsonResultObj checkMesCode(String mobile, String mesCode)
    {
        LOGGER.info("开始校验短信验证码是否正确 mobile={} mesCode={}",mobile,mesCode);
        JsonResultObj resultObj = null;
        try
        {
            String storedMesCode = mobileService.loadMesCodeByMobile(mobile);
            if(storedMesCode == null)
            {
                resultObj = new JsonResultObj(false, JsonResultEnum.USER_MESCODE_EXPIRED);
            }
            else if(!storedMesCode.equals(mesCode))
            {
                resultObj = new JsonResultObj(false, JsonResultEnum.USER_MESCODE_ERROR);
            }
            else
            {
                mobileService.setMesCodeInvalid(mobile);
                resultObj = new JsonResultObj(true);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            resultObj = new JsonResultObj(false);
        }
        LOGGER.info("结束校验短信验证码是否正确 mobile={} mesCode={}",mobile,mesCode);
        return resultObj;
    }

    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="注册用户")
    @ApiImplicitParams({@ApiImplicitParam(name="username", value="用户名", required=true, dataType="String"),
            @ApiImplicitParam(name="password", value="密码", required=true, dataType="String"),
            @ApiImplicitParam(name="mobile", value="手机号", required=true, dataType="String"),
            @ApiImplicitParam(name="mesCode", value="短信验证码", required=true, dataType="String")
    })
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：注册成功 isSuccess=false：注册失败，resMsg为错误信息")})
    public JsonResultObj addUser(String username, String password, String mobile, String mesCode)
    {
        LOGGER.info("开始新增用户");
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
        LOGGER.info("结束新增用户");
        return resultObj;
    }


}
