package com.xyc.userc.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.avei.shriety.wx_sdk.pojo.Userinfo;
import com.xyc.userc.entity.CarNumOpenId;
import com.xyc.userc.entity.Role;
import com.xyc.userc.entity.User;
import com.xyc.userc.service.CarNumService;
import com.xyc.userc.service.MobileService;
import com.xyc.userc.service.UserService;
import com.xyc.userc.util.BusinessException;
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
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by 1 on 2020/8/11.
 */
@RestController
@CrossOrigin
@RequestMapping("/mes")
@Api(tags = "系统用户管理相关api")
public class TemplateController
{
	protected static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);

	@Resource
	UserService userService;

	@Resource
	MobileService mobileService;

	@Resource
	CarNumService carNumService;

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

	@PostMapping("/bindMobile")
	@ApiOperation(value="绑定手机号")
    @ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "mesCode", value = "短信验证码", required = true, dataType = "String")})
	@ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：绑定成功 isSuccess=false：绑定失败，resMsg为错误信息")})
	public JsonResultObj bindMobile(HttpSession session, String mobile, String mesCode)
	{
		LOGGER.info("开始绑定手机号 mobile={},mesCode={}",mobile,mesCode);
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
			Role role = userService.bindMobileToOpenId(mobile,mesCode,openId);
			List<Role> roleList = new ArrayList<Role>();
			roleList.add(role);
			user.setRoles(roleList);
			user.setMobilePhone(mobile);
			session.setAttribute("USERINFOANDROLES",user);
			resultObj = new JsonResultObj(true,user);
		}
		catch (Exception e)
		{
		    if(e instanceof BusinessException)
            {
                LOGGER.error("绑定手机号失败：{}", ((BusinessException) e).getJsonResultEnum().getMessage());
                resultObj = new JsonResultObj(false,((BusinessException) e).getJsonResultEnum());
            }
            else
            {
                e.printStackTrace();
                LOGGER.error("绑定手机号失败：{}", e.getMessage());
                resultObj = new JsonResultObj(false);
            }
		}
		LOGGER.info("结束绑定手机号 mobile={}",mobile);
		return resultObj;
	}

	//发送验证码
	@RequestMapping(value = "/sendMesCode",method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="发送短信验证码")
	@ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String")
	@ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：发送成功 isSuccess=false：发送失败，resMsg为错误信息")})
	public JsonResultObj sendMesCode(String mobile)
	{
		LOGGER.info("开始生成发送短信验证码 mobile={}",mobile);
		JsonResultObj resultObj = null;
		try
		{
			mobileService.checkAndSendMesCode(mobile);
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

	@PostMapping("/queryCarNum")
	@ApiOperation(value="查询车牌号")
	@ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号", required = false, dataType = "String"),
			@ApiImplicitParam(name = "carNum", value = "车牌号", required = false, dataType = "String")})
	@ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
	public JsonResultObj queryCarNum(String mobile, String carNum)
	{
		LOGGER.info("开始查询车牌号mobile={},carNum={}",mobile,carNum);
		JsonResultObj resultObj = null;
		try
		{
			List<CarNumOpenId> carNumOpenIdList = carNumService.getCarNum(mobile,carNum);
			resultObj = new JsonResultObj(true,carNumOpenIdList);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("查询车牌号失败：{}",e.getMessage());
			resultObj = new JsonResultObj(false);
		}
		LOGGER.info("结束查询车牌号");
		return resultObj;
	}

    @PostMapping("/deleteCarNum")
    @ApiOperation(value="删除车牌号")
    @ApiImplicitParam(name = "carNum", value = "车牌号", required = true, dataType = "String")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：删除成功 isSuccess=false：删除失败，resMsg为错误信息")})
    public JsonResultObj deleteCarNum(String carNum, HttpSession session)
    {
        LOGGER.info("开始删除车牌号 carNum={}",carNum);
        JsonResultObj resultObj = null;
        User user = (User) session.getAttribute(WxsdkConstant.USERINFO);
//            if(user == null)
//            {
//                user = new User();
//                user.setOpenid("oPh4us4mI1Egdf_aCs9PzL8j9qLY");
//            }
        if(user == null)
        {
            LOGGER.info("session中不存在用户信息");
            resultObj = new JsonResultObj(false, JsonResultEnum.USER_INFO_NOT_EXIST);
            return resultObj;
        }
        String openId = user.getOpenid();
        try
        {
			carNumService.removeCarNum(carNum,openId);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
			if(e instanceof BusinessException)
			{
				LOGGER.error("删除车牌号失败：{}", ((BusinessException)e).getJsonResultEnum().getMessage());
				resultObj = new JsonResultObj(false,((BusinessException)e).getJsonResultEnum());
			}
			else
			{
				e.printStackTrace();
				LOGGER.error("删除车牌号失败：{}",e.getMessage());
				resultObj = new JsonResultObj(false);
			}
        }
        LOGGER.info("结束删除车牌号");
        return resultObj;
    }

	@PostMapping("/addCarNum")
	@ApiOperation(value="新增车牌号")
	@ApiImplicitParam(name = "carNum", value = "车牌号", required = true, dataType = "String")
	@ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：新增成功 isSuccess=false：新增失败，resMsg为错误信息")})
	public JsonResultObj addCarNum(String carNum, HttpSession session)
	{
		LOGGER.info("开始新增车牌号 carNum={}", carNum);
		JsonResultObj resultObj = null;
		User user = (User) session.getAttribute(WxsdkConstant.USERINFO);
//            if(user == null)
//            {
//                user = new User();
//                user.setOpenid("oPh4us4mI1Egdf_aCs9PzL8j9qLY");
//            }
		if(user == null)
		{
			LOGGER.info("session中不存在用户信息");
			resultObj = new JsonResultObj(false, JsonResultEnum.USER_INFO_NOT_EXIST);
			return resultObj;
		}
		String openId = user.getOpenid();
		try
		{
			carNumService.addCarNum(carNum,openId);
			resultObj = new JsonResultObj(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("新增车牌号失败：{}",e.getMessage());
			resultObj = new JsonResultObj(false);
		}
		LOGGER.info("结束新增车牌号 carNum={}", carNum);
		return resultObj;
	}

	@PostMapping("/updateCarNum")
	@ApiOperation(value="修改车牌号")
	@ApiImplicitParams({@ApiImplicitParam(name="oldCarNum", value="原来的车牌号", required=true, dataType="String"),
			@ApiImplicitParam(name="newCarNum", value="新的车牌号", required=true, dataType="String")})
	@ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：修改成功 isSuccess=false：修改失败，resMsg为错误信息")})
	public JsonResultObj updateCarNum(String oldCarNum, String newCarNum, HttpSession session) {
		LOGGER.info("开始修改车牌号 oldCarNum={} newCarNum={}", oldCarNum, newCarNum);
		JsonResultObj resultObj = null;
		User user = (User) session.getAttribute(WxsdkConstant.USERINFO);
//            if(user == null)
//            {
//                user = new User();
//                user.setOpenid("oPh4us4mI1Egdf_aCs9PzL8j9qLY");
//            }
		if (user == null) {
			LOGGER.info("session中不存在用户信息");
			resultObj = new JsonResultObj(false, JsonResultEnum.USER_INFO_NOT_EXIST);
			return resultObj;
		}
		String openId = user.getOpenid();

		try {
			carNumService.modifyCarNumByOpenId(oldCarNum, newCarNum, openId);
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				LOGGER.error("修改车牌号失败：{}", ((BusinessException) e).getJsonResultEnum().getMessage());
				resultObj = new JsonResultObj(false, ((BusinessException) e).getJsonResultEnum());
			} else {
				e.printStackTrace();
				LOGGER.error("修改车牌号失败：{}", e.getMessage());
				resultObj = new JsonResultObj(false);
			}
		}
		LOGGER.info("结束修改车牌号 oldCarNum={} newCarNum={}", oldCarNum, newCarNum);
		return resultObj;
	}
}
