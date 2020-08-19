package com.xyc.userc.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.avei.shriety.wx_sdk.pojo.Userinfo;
import com.xyc.userc.entity.Role;
import com.xyc.userc.entity.User;
import com.xyc.userc.service.MobileService;
import com.xyc.userc.service.UserService;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.JsonResultObj;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.avei.shriety.wx_sdk.constant.WxsdkConstant;
import com.avei.shriety.wx_sdk.pojo.ReturnData;
import com.avei.shriety.wx_sdk.template.Template;
import smsservice.ISMS;
import smsservice.SMSService;

/**
 * Created by 1 on 2020/8/11.
 */
@RestController
@CrossOrigin
@RequestMapping("/mes")
@Api(tags = "系统用户管理相关api")
public class TemplateController
{
	protected static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Resource
	UserService userService;

	@Resource
	MobileService mobileService;

	@GetMapping("/template")
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

	@GetMapping("/userinfo")
	@ApiOperation(value="获取当前用户信息")
	@ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：获取成功 isSuccess=false：获取失败，resMsg为错误信息")})
	public JsonResultObj userinfo(HttpSession session)
	{
		LOGGER.info("开始获取当前用户信息");
		JsonResultObj resultObj;
		User user = null;
		try
		{
			user = (User)session.getAttribute("USERINFOANDROLES");
			if(user != null)
			{
				resultObj = new JsonResultObj(true,user);
			}
			else
			{
				user = (User) session.getAttribute(WxsdkConstant.USERINFO);
				if(user == null)
				{
					LOGGER.info("session中不存在用户信息");
					resultObj = new JsonResultObj(false, JsonResultEnum.USER_INFO_NOT_EXIST);
				}
				else
				{
//					if(user == null)
//					{
//						user = new User();
//						user.setOpenid("oPh4us4mI1Egdf_aCs9PzL8j9qLY");
//					}
					user = userService.getUser(user);
					resultObj = new JsonResultObj(true,user);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("获取用户信息失败：{}",e.getMessage());
			resultObj = new JsonResultObj(false);
		}
		LOGGER.info("结束获取当前用户信息userinfo: {}",user);
		return resultObj;
	}

	@GetMapping("/bindMobile")
	@ApiOperation(value="绑定手机号")
	@ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String")
	@ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：绑定成功 isSuccess=false：绑定失败，resMsg为错误信息")})
	public JsonResultObj bindMobile(HttpSession session, String mobile)
	{
		LOGGER.info("开始绑定手机号 mobile={}", mobile);
		JsonResultObj resultObj;
		try
		{
			User user = (User) session.getAttribute(WxsdkConstant.USERINFO);
//            if(user == null)
//            {
//                user = new User();
//                user.setOpenid("oPh4us4mI1Egdf_aCs9PzL8j9qLY");
//            }
			if (user == null)
			{
				LOGGER.info("session中不存在用户信息");
				resultObj = new JsonResultObj(false, JsonResultEnum.USER_INFO_NOT_EXIST);
				return resultObj;
			}
			String openId = user.getOpenid();
			Role role = userService.bindMobileToOpenId(mobile, openId);
			List<Role> roleList = new ArrayList<Role>();
			roleList.add(role);
			user.setRoles(roleList);
			user.setMobilePhone(mobile);
			session.setAttribute("USERINFOANDROLES",user);
			resultObj = new JsonResultObj(true,user);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("绑定手机号失败：{}", e.getMessage());
			resultObj = new JsonResultObj(false);
		}
		LOGGER.info("结束绑定手机号 mobile={}",mobile);
		return resultObj;
	}

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
}
