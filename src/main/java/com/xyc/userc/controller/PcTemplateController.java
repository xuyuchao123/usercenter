package com.xyc.userc.controller;

import com.xyc.userc.security.MesCodeErrorException;
import com.xyc.userc.security.MesCodeExpiredException;
import com.xyc.userc.service.MobileService;
import com.xyc.userc.service.UserService;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.JsonResultObj;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import smsservice.ISMS;
import smsservice.SMSService;

import javax.annotation.Resource;
import java.util.Random;

/**
 * Created by 1 on 2020/10/15.
 */
@RestController
@CrossOrigin
@RequestMapping("/mes")
@Api(tags = "pc端用户登陆相关api")
public class PcTemplateController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(PcTemplateController.class);

    @Resource
    UserService userService;

    @Resource
    MobileService mobileService;

    //pc端发送验证码
    @RequestMapping(value = "/sendMesCode_pc",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="pc端发送短信验证码")
    @ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType =  "String"),
            @ApiImplicitParam(name = "type", value = "操作类型", required = true, dataType =  "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：发送成功 isSuccess=false：发送失败，resMsg为错误信息")})
    public JsonResultObj sendMesCode(String mobile, String type)
    {
        LOGGER.debug("开始生成发送pc端短信验证码 mobile={},type={}",mobile,type);
        JsonResultObj resultObj = null;
        try
        {
            if(!"register".equals(type))
            {
                //检查手机号是否存在,mobileExist=1表示存在
                int mobileExist =  userService.checkUserExistByMobile(mobile);
                if(mobileExist != 1)
                {
                    LOGGER.error("pc端短信验证码生成发送失败，手机号不存在 mobile={}",mobile);
                    resultObj = new JsonResultObj(false, JsonResultEnum.USER_MOBILE_NOT_EXIST);
                    return resultObj;
                }
            }

            String mesCode = null;
            //检查是否存在有效的短信验证码
            mesCode = mobileService.loadPcMesCodeByMobile(mobile);
            if(mesCode == null)
            {
                //不存在有效验证码就随机生成一个6位数短信验证码
                mesCode = String.valueOf(new Random().nextInt(899999) + 100000);
                //将新生成的验证码存入数据库
                mobileService.addMesCode(mobile, mesCode);
            }
            LOGGER.debug("成功生成pc端短信验证码 mesCode={}",mesCode);
            //发送验证码短信功能。。。
            ISMS smsService = new SMSService().getSMSImplPort();
            smsService.smsSend(mobile,"验证码：" + mesCode);
            resultObj = new JsonResultObj(true);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("pc端短信验证码生成发送失败：{}",e.getMessage());
            resultObj = new JsonResultObj(false);
        }
        LOGGER.debug("结束生成发送pc端短信验证码");
        return resultObj;
    }

    //重置pc端用户密码
    @RequestMapping(value = "/resetPassword",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="pc端用户重置密码")
    @ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, dataType = "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：重置密码成功 isSuccess=false：重置密码失败，resMsg为错误信息")})
    public JsonResultObj resetPassword(String mobile, String newPassword)
    {
        LOGGER.debug("开始pc端重置密码 mobile={} newPassword={}",mobile,newPassword);
        JsonResultObj resultObj = null;
        try
        {
            userService.updatePassword(mobile,newPassword);
            resultObj = new JsonResultObj(true);
            LOGGER.debug("pc端重置密码成功 mobile={} newPassword={}",mobile,newPassword);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            LOGGER.error("pc端重置密码失败：",e.getMessage());
            resultObj = new JsonResultObj(false);
        }
        LOGGER.debug("结束pc端重置密码 mobile={} newPassword={}",mobile,newPassword);
        return resultObj;
    }

    //测试pc端用户名是否已注册
    @RequestMapping(value = "/checkUsernameExist",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="pc端测试用户名是否已注册")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：用户名未被注册 isSuccess=false：操作失败或用户名已被注册，resMsg为错误信息")})
    public JsonResultObj checkUsernameExist(String username)
    {
        LOGGER.debug("开始检查pc端用户名是否已注册 username={}",username);
        JsonResultObj resultObj = null;
        try
        {
            int usernameExist = userService.checkUserRegByUsername(username);
            if(usernameExist == 0)
            {
                LOGGER.debug("pc端用户名未注册 username={}",username);
                resultObj = new JsonResultObj(true);
            }
            else
            {
                LOGGER.debug("pc端用户名已注册 username={}",username);
                resultObj = new JsonResultObj(false,JsonResultEnum.USER_ACCOUNT_EXIST);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            LOGGER.error("检查pc端用户名是否已注册失败：",e.getMessage());
            resultObj = new JsonResultObj(false);
        }
        LOGGER.debug("结束检查pc端用户名是否已注册 username={}",username);
        return resultObj;

    }

    //测试pc端手机号是否已注册
    @RequestMapping(value = "/checkMobileExist",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="pc端测试手机号是否已注册")
    @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String")
    @ApiResponses({@ApiResponse(code = 200, message = "isSuccess=true：手机号未被注册 isSuccess=false：操作失败或手机号已被注册，resMsg为错误信息")})
    public JsonResultObj checkMobileExist(String mobile)
    {
        LOGGER.debug("开始检查pc端手机号是否已注册 mobile={}",mobile);
        JsonResultObj resultObj = null;
        try
        {
            int mobileExist = userService.checkUserRegByMobile(mobile);
            if(mobileExist == 0)
            {
                LOGGER.debug("pc端手机号未注册 mobile={}",mobile);
                resultObj = new JsonResultObj(true);
            }
            else
            {
                LOGGER.debug("pc端手机号已注册 mobile={}",mobile);
                resultObj = new JsonResultObj(false,JsonResultEnum.USER_MOBILE_EXIST);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            LOGGER.error("检查pc端手机号是否已注册失败：",e.getMessage());
            resultObj = new JsonResultObj(false);
        }
        LOGGER.debug("结束检查pc端用户名是否已注册 mobile={}",mobile);
        return resultObj;
    }

    //校验pc端短信验证码是否正确
    @RequestMapping(value = "/checkMesCode",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="pc端校验短信验证码是否正确")
    @ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "mesCode", value = "验证码", required = true, dataType = "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：验证码正确 isSuccess=false：操作失败或验证码过期/错误，resMsg为错误信息")})
    public JsonResultObj checkMesCode(String mobile, String mesCode)
    {
        LOGGER.debug("开始校验pc端短信验证码是否正确 mobile={} mesCode={}",mobile,mesCode);
        JsonResultObj resultObj = null;
        try
        {
            String storedMesCode = mobileService.loadPcMesCodeByMobile(mobile);
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
        LOGGER.debug("结束校验pc端短信验证码是否正确 mobile={} mesCode={}",mobile,mesCode);
        return resultObj;
    }

    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="pc端注册用户")
    @ApiImplicitParams({@ApiImplicitParam(name="username", value="用户名", required=true, dataType="String"),
            @ApiImplicitParam(name="password", value="密码", required=true, dataType="String"),
            @ApiImplicitParam(name="mobile", value="手机号", required=true, dataType="String"),
            @ApiImplicitParam(name="mesCode", value="短信验证码", required=true, dataType="String")
    })
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：注册成功 isSuccess=false：注册失败，resMsg为错误信息")})
    public JsonResultObj addUser(String username, String password, String mobile, String mesCode)
    {
        LOGGER.debug("开始新增pc端用户");
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
            LOGGER.error("新增pc端用户失败：{}",e.getMessage());
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
        LOGGER.debug("结束新增pc端用户");
        return resultObj;
    }

}
