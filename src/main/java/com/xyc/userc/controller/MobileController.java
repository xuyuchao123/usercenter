package com.xyc.userc.controller;

import com.alibaba.fastjson.JSONObject;
import com.xyc.userc.service.MobileService;
import com.xyc.userc.service.UserService;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.JsonResultObj;
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
    public Object addUser(String mobile)
    {
        LOGGER.debug("开始生成发送短信验证码 mobile={}",mobile);
        Object jsonObj = null;
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
                JsonResultObj resultObj = new JsonResultObj(true);
                jsonObj = JSONObject.toJSON(resultObj);
            }
            else
            {
                LOGGER.error("短信验证码生成发送失败，手机号不存在 mobile={}",mobile);
                JsonResultObj resultObj = new JsonResultObj(false, JsonResultEnum.USER_MOBILE_NOT_EXIST);
                jsonObj = JSONObject.toJSON(resultObj);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("短信验证码生成发送失败：{}",e.getMessage());
            JsonResultObj resultObj = new JsonResultObj(false);
            jsonObj = JSONObject.toJSON(resultObj);
        }
        LOGGER.debug("结束生成发送短信验证码");
        return jsonObj;
    }

    //重置密码
    @RequestMapping(value = "/resetPassword",method = RequestMethod.GET)
    @ResponseBody
    public Object resetPassword(String mobile, String newPassword)
    {
        LOGGER.debug("开始重置密码 mobile={} newPassword={}",mobile,newPassword);
        Object jsonObj = null;
        try
        {
            userService.updatePassword(mobile,newPassword);
            JsonResultObj resultObj = new JsonResultObj(true);
            jsonObj = JSONObject.toJSON(resultObj);
            LOGGER.debug("重置密码成功 mobile={} newPassword={}",mobile,newPassword);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            LOGGER.error("重置密码失败：",e.getMessage());
            JsonResultObj resultObj = new JsonResultObj(false);
            jsonObj = JSONObject.toJSON(resultObj);
        }
        LOGGER.debug("结束重置密码 mobile={} newPassword={}",mobile,newPassword);
        return jsonObj;
    }

    //测试用户名是否已注册
    @RequestMapping(value = "/checkUsernameExist",method = RequestMethod.POST)
    @ResponseBody
    public Object checkUsernameExist(String username)
    {
        LOGGER.debug("开始检查用户名是否已注册 username={}",username);
        Object jsonObj = null;
        try
        {
            int usernameExist = userService.checkUserRegByUsername(username);
            if(usernameExist == 0)
            {
                LOGGER.debug("用户名未注册 username={}",username);
                JsonResultObj resultObj = new JsonResultObj(true);
                jsonObj = JSONObject.toJSON(resultObj);
            }
            else
            {
                LOGGER.debug("用户名已注册 username={}",username);
                JsonResultObj resultObj = new JsonResultObj(false,JsonResultEnum.USER_ACCOUNT_EXIST);
                jsonObj = JSONObject.toJSON(resultObj);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            LOGGER.error("检查用户名是否已注册失败：",e.getMessage());
            JsonResultObj resultObj = new JsonResultObj(false);
            jsonObj = JSONObject.toJSON(resultObj);
        }
        LOGGER.debug("结束检查用户名是否已注册 username={}",username);
        return jsonObj;

    }

    //测试手机号是否已注册
    @RequestMapping(value = "/checkMobileExist",method = RequestMethod.POST)
    @ResponseBody
    public Object checkMobileExist(String mobile)
    {
        LOGGER.debug("开始检查手机号是否已注册 mobile={}",mobile);
        Object jsonObj = null;
        try
        {
            int mobileExist = userService.checkUserRegByMobile(mobile);
            if(mobileExist == 0)
            {
                LOGGER.debug("手机号未注册 mobile={}",mobile);
                JsonResultObj resultObj = new JsonResultObj(true);
                jsonObj = JSONObject.toJSON(resultObj);
            }
            else
            {
                LOGGER.debug("手机号已注册 mobile={}",mobile);
                JsonResultObj resultObj = new JsonResultObj(false,JsonResultEnum.USER_MOBILE_EXIST);
                jsonObj = JSONObject.toJSON(resultObj);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            LOGGER.error("检查手机号是否已注册失败：",e.getMessage());
            JsonResultObj resultObj = new JsonResultObj(false);
            jsonObj = JSONObject.toJSON(resultObj);
        }
        LOGGER.debug("结束检查用户名是否已注册 mobile={}",mobile);
        return jsonObj;
    }

}
