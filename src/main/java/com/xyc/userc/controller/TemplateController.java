package com.xyc.userc.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.xyc.userc.entity.Role;
import com.xyc.userc.entity.User;
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
	public ReturnData templateSend() {
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
//	@ApiOperation(value = "获取当前用户信息")
//	@ApiResponses({@ApiResponse(code = 200, message = "isSuccess=true：获取成功 isSuccess=false：获取失败，resMsg为错误信息")})
	public JsonResultObj getUserinfo(@ApiIgnore HttpSession session) {
		LOGGER.info("开始获取当前用户信息");
		JsonResultObj resultObj = null;
		User user = null;
		try {
			user = (User) session.getAttribute("USERINFOANDROLES");
			if (user != null) {
				resultObj = new JsonResultObj(true, user);
			} else {
				user = (User) session.getAttribute(WxsdkConstant.USERINFO);
				if (user == null) {
					user = new User("oPh4uszJ0L7a9zNRU-tw4smPtbfU", "一人！一车！一世界！", "1", "山东", "临沂", "中国",
							"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJPHH4qzibNINxpqxUnZEeibiagxgibibiaB" +
									"2EM9DXt7CLNpgmjewP5lsIoR0HQ1Cqzq46K1Dz93jdAQj4g/132", null, null, "13167068999", null);
//                    user.setCarNum("豫R97829");
				}
				if (user == null) {
					LOGGER.info("session中不存在用户信息");
					resultObj = new JsonResultObj(false, JsonResultEnum.USER_INFO_NOT_EXIST);
				} else {
					user = userService.getUser(user);
					session.setAttribute("USERINFOANDROLES", user);
					resultObj = new JsonResultObj(true, user);
				}
			}
		} catch (Exception e) {
			resultObj = CommonExceptionHandler.handException(e, "获取用户信息失败", LOGGER, resultObj);
		}
		LOGGER.info("结束获取当前用户信息userinfo: {}", user);
		return resultObj;
	}

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
	public JsonResultObj sendMesCode(String mobile) {
		LOGGER.info("开始生成发送短信验证码 mobile={}", mobile);
		JsonResultObj resultObj = null;
		try {
			mobileService.checkAndSendMesCode(mobile);
			resultObj = new JsonResultObj(true);
		} catch (Exception e) {
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
}
