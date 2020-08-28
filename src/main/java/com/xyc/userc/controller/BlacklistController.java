package com.xyc.userc.controller;

import com.avei.shriety.wx_sdk.constant.WxsdkConstant;
import com.xyc.userc.entity.User;
import com.xyc.userc.service.BlacklistService;
import com.xyc.userc.util.CommonExceptionHandler;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.JsonResultObj;
import com.xyc.userc.vo.BlacklistVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by 1 on 2020/8/24.
 */
@RestController
@CrossOrigin
@RequestMapping("/mes")
@Api(tags = "黑名单管理相关api")
public class BlacklistController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);

    @Resource
    BlacklistService blacklistService;

    @PostMapping("/queryBlacklist")
    @ApiOperation(value="查询黑名单")
    @ApiImplicitParams({@ApiImplicitParam(name="name", value="被拉入黑名单的人的昵称", required=true, dataType="String"),
            @ApiImplicitParam(name="mobile", value="被拉入黑名单的人的手机号", required=true, dataType="String"),
            @ApiImplicitParam(name="createName", value="黑名创建人的昵称", required=true, dataType="String"),
            @ApiImplicitParam(name="createMobile", value="黑名创建人的手机号", required=true, dataType="String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj queryBlacklist(String name, String mobile, String createName,
                                        String createMobile, @ApiIgnore HttpSession session)
    {
        LOGGER.info("开始查询黑名单 name={} mobile={} createName={} createMobile={}",name,mobile,createName,createMobile);
        JsonResultObj resultObj = null;
        try
        {
            List<BlacklistVo> blacklistVoList = blacklistService.getBlacklist(name,mobile,createName,createMobile);
            resultObj = new JsonResultObj(true,blacklistVoList);
            LOGGER.info("查询黑名单成功，查询结果：{}",blacklistVoList.toString());
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "查询黑名单失败", LOGGER, resultObj);
        }
        LOGGER.info("结束查询黑名单 name={} mobile={} createName={} createMobile={}",name,mobile,createName,createMobile);
        return resultObj;
    }

    @PostMapping("/addBlacklist")
    @ApiOperation(value="新增黑名单")
    @ApiImplicitParams({@ApiImplicitParam(name="mobile", value="被拉入黑名单的人的手机号", required=true, dataType="String"),
            @ApiImplicitParam(name="reason", value="拉黑原因", required=true, dataType="String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：新增成功 isSuccess=false：新增失败，resMsg为错误信息")})
    public JsonResultObj addBlacklist(String mobile, String reason, @ApiIgnore HttpSession session)
    {
        LOGGER.info("开始新增黑名单 mobile={} reason={}", mobile,reason);
        JsonResultObj resultObj = null;
        User user = (User) session.getAttribute(WxsdkConstant.USERINFO);
        if(user == null)
        {
            user = new User("oPh4uszJ0L7a9zNRU-tw4smPtbfU","一人！一车！一世界！","1","山东","临沂","中国",
                    "http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJPHH4qzibNINxpqxUnZEeibiagxgibibiaB" +
                            "2EM9DXt7CLNpgmjewP5lsIoR0HQ1Cqzq46K1Dz93jdAQj4g/132",null,null,"13167068999",null);
        }
        if (user == null) {
            LOGGER.info("session中不存在用户信息");
            resultObj = new JsonResultObj(false, JsonResultEnum.USER_INFO_NOT_EXIST);
            return resultObj;
        }
        String openId = user.getOpenid();
        try
        {
            blacklistService.addBlacklist(mobile,reason,openId);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "新增黑名单失败", LOGGER, resultObj);
        }
        LOGGER.info("结束新增黑名单 mobile={} reason={}", mobile,reason);
        return  resultObj;
    }

    @PostMapping("/deleteBlacklist")
    @ApiOperation(value="删除黑名单")
    @ApiImplicitParam(name="mobile", value="被拉入黑名单的人的手机号", required=true, dataType="String")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：删除成功 isSuccess=false：删除失败，resMsg为错误信息")})
    public JsonResultObj deleteBlacklist(String mobile)
    {
        LOGGER.info("开始删除黑名单 mobile={}", mobile);
        JsonResultObj resultObj = null;
        try
        {
            blacklistService.removeBlacklist(mobile);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "删除黑名单失败", LOGGER, resultObj);
        }
        LOGGER.info("结束删除黑名单 mobile={}", mobile);
        return resultObj;
    }
}
