package com.xyc.userc.controller;
import com.xyc.userc.entity.PcUser;
import com.xyc.userc.service.UserService;
import com.xyc.userc.util.CommonExceptionHandler;
import com.xyc.userc.util.JsonResultObj;
import com.xyc.userc.vo.OpenIdNickNameVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by 1 on 2020/7/2.
 * 用户操作控制器
 */

@Controller
//@CrossOrigin("http://localhost:8080")
@RequestMapping("/user")
@Api(tags = "pc端用户管理相关api")
public class UserController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Resource
    UserService userService;


    @RequestMapping(value = "/getCurrentUser",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="获取当前用户信息")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：获取成功 isSuccess=false：获取失败，resMsg为错误信息")})
    public JsonResultObj getCurrentUser(@ApiIgnore HttpSession session)
    {
        String sessionId = null;
        if(session != null)
        {
            sessionId = session.getId();
        }
        LOGGER.debug("开始获取当前用户信息 sessionId:{}",sessionId);

        PcUser pcUser = (PcUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println(user);
        JsonResultObj resultObj = new JsonResultObj(true,pcUser);
        LOGGER.debug("结束获取当前用户信息");
        return resultObj;
    }

//    @RequestMapping(value = "/queryOpenIdNickName",method = RequestMethod.POST)
//    @ResponseBody
//    @ApiOperation(value="根据工号获取openid")
//    @ApiImplicitParam(name = "jobNum", value = "工号", required = true, dataType = "String")
//    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：获取成功 isSuccess=false：获取失败，resMsg为错误信息")})
//    public JsonResultObj queryOpenIdNickName(String jobNum)
//    {
//        LOGGER.info("开始根据工号获取 jobNum={}" ,jobNum);
//        JsonResultObj resultObj = null;
//        try
//        {
//            List<String> openIdNickNameVoList = userService.getOpenIdNickName(jobNum);
//            resultObj = new JsonResultObj(true);
//        }
//        catch (Exception e)
//        {
//            resultObj = CommonExceptionHandler.handException(e, "根据工号获取openid,nickname失败", LOGGER, resultObj);
//        }
//        LOGGER.info("结束根据工号获取openid mobile={} role={}" ,jobNum);
//        return resultObj;
//    }


}
