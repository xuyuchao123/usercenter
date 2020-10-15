package com.xyc.userc.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.xyc.userc.entity.Role;
import com.xyc.userc.entity.User;
import com.xyc.userc.security.MesCodeErrorException;
import com.xyc.userc.security.MesCodeExpiredException;
import com.xyc.userc.service.MobileService;
import com.xyc.userc.service.UserService;
import com.xyc.userc.util.CommonExceptionHandler;
import com.xyc.userc.util.UsercConstant;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.JsonResultObj;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.avei.shriety.wx_sdk.constant.WxsdkConstant;
import com.avei.shriety.wx_sdk.pojo.ReturnData;
import com.avei.shriety.wx_sdk.template.Template;
import smsservice.ISMS;
import smsservice.SMSService;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by 1 on 2020/8/11.
 */
@RestController
@CrossOrigin
@RequestMapping("/mes")
@Api(tags = "系统用户管理相关api")
public class TemplateController {
	protected static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);

	@Resource
	UserService userService;

	@Resource
	MobileService mobileService;

	@GetMapping("/template")
	@ApiIgnore
	public ReturnData templateSend()
	{
		// oPh4us7phJe_qUG8o1TGY4Mrd2Yg 氕氘氚
		// oPh4us_Fe88F-HgRKn9QAYQTBzOs Pluto
		// oPh4us__A3ShBKk7fkxu2ehE_OWI 喂喂

		Map<String, String> propsMap = new HashMap<String, String>();
		propsMap.put("TOUSER", "oPh4us_Fe88F-HgRKn9QAYQTBzOs");
		propsMap.put("TEMPLATE_ID", "CMzvZBeSWTAYG7qp7B8CwTvc2tT4fwNQO8cQynQ2OCA");
		propsMap.put("FIRST", "测试数据");
		propsMap.put("KEYWORD1", "测试数据");
		propsMap.put("KEYWORD2", "测试数据");
		propsMap.put("KEYWORD3", "测试数据");
		propsMap.put("KEYWORD4", "测试数据");
		propsMap.put("REMARK", "测试数据");
		return Template.send(propsMap);
	}

//	@GetMapping("/userinfo")
//	@ApiOperation(value = "获取当前用户信息")
//	@ApiResponses({@ApiResponse(code = 200, message = "isSuccess=true：获取成功 isSuccess=false：获取失败，resMsg为错误信息")})
//	public JsonResultObj getUserinfo(@ApiIgnore HttpSession session)
//	{
//		LOGGER.info("开始获取当前用户信息");
//		JsonResultObj resultObj = null;
//		User user = null;
//		try {
//			user = (User) session.getAttribute("USERINFOANDROLES");
//			if (user != null) {
//				resultObj = new JsonResultObj(true, user);
//			} else {
//				user = (User) session.getAttribute(WxsdkConstant.USERINFO);
//				if (user == null) {
//					user = new User("oPh4uszJ0L7a9zNRU-tw4smPtbfU", "一人！一车！一世界！", "1", "山东", "临沂", "中国",
//							"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJPHH4qzibNINxpqxUnZEeibiagxgibibiaB" +
//									"2EM9DXt7CLNpgmjewP5lsIoR0HQ1Cqzq46K1Dz93jdAQj4g/132", null, null, "13167068999", null);
////                    user.setCarNum("豫R97829");
//				}
//				if (user == null) {
//					LOGGER.info("session中不存在用户信息");
//					resultObj = new JsonResultObj(false, JsonResultEnum.USER_INFO_NOT_EXIST);
//				} else {
//					user = userService.getUser(user);
//					session.setAttribute("USERINFOANDROLES", user);
//					resultObj = new JsonResultObj(true, user);
//				}
//			}
//		} catch (Exception e) {
//			resultObj = CommonExceptionHandler.handException(e, "获取用户信息失败", LOGGER, resultObj);
//		}
//		LOGGER.info("结束获取当前用户信息userinfo: {}", user);
//		return resultObj;
//	}

	@PostMapping("/bindMobile")
	@ApiOperation(value = "绑定手机号")
	@ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String"),
			@ApiImplicitParam(name = "mesCode", value = "短信验证码", required = true, dataType = "String")})
	@ApiResponses({@ApiResponse(code = 200, message = "isSuccess=true：绑定成功 isSuccess=false：绑定失败，resMsg为错误信息")})
	public JsonResultObj bindMobile(@ApiIgnore HttpServletRequest request, String mobile, String mesCode)
    {
		LOGGER.info("开始绑定手机号 mobile={},mesCode={}", mobile, mesCode);
        JsonResultObj resultObj = null;
		String openId = request.getHeader(UsercConstant.OPENID);
        if(openId == null)
        {
            LOGGER.info("未获取到用户的openId");
            resultObj = new JsonResultObj(false, JsonResultEnum.USER_INFO_NOT_EXIST);
        }
        else
        {
            try
            {
                User user = userService.bindMobileToOpenId(mobile, mesCode, openId);
                resultObj = new JsonResultObj(true, user);
            }
            catch (Exception e)
            {
                resultObj = CommonExceptionHandler.handException(e, "绑定手机号失败", LOGGER, resultObj);
            }
        }
		LOGGER.info("结束绑定手机号 mobile={}", mobile);
		return resultObj;
	}

	//发送验证码
	@RequestMapping(value = "/sendMesCode", method = RequestMethod.POST)
	@ApiOperation(value = "发送短信验证码")
	@ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String")
	@ApiResponses({@ApiResponse(code = 200, message = "isSuccess=true：发送成功 isSuccess=false：发送失败，resMsg为错误信息")})
	public JsonResultObj sendMesCode(String mobile)
	{
		LOGGER.info("开始生成发送短信验证码 mobile={}", mobile);
		JsonResultObj resultObj = null;
		try
		{
			mobileService.checkAndSendMesCode(mobile);
			resultObj = new JsonResultObj(true);
		}
		catch (Exception e)
		{
			resultObj = CommonExceptionHandler.handException(e, "短信验证码生成发送失败", LOGGER, resultObj);
		}
		LOGGER.info("结束生成发送短信验证码");
		return resultObj;
	}

	@GetMapping("/resetUserInfo")
	@ApiOperation(value = "重置redis中的用户信息")
	@ApiResponses({@ApiResponse(code = 200, message = "isSuccess=true：重置成功 isSuccess=false：重置失败，resMsg为错误信息")})
	public JsonResultObj resetUserInfo()
	{
		LOGGER.info("开始重置redis中的用户信息");
		JsonResultObj resultObj = null;
		try
		{
			userService.storeUserInfoVo();
			resultObj = new JsonResultObj(true);
		}
		catch (Exception e)
		{
			resultObj = CommonExceptionHandler.handException(e, "重置redis中的用户信息失败", LOGGER, resultObj);
		}
		LOGGER.info("结束重置redis中的用户信息");
		return resultObj;
	}

	//******************************************************************

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
