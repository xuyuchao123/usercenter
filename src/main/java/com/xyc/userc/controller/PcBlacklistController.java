package com.xyc.userc.controller;

import com.avei.shriety.wx_sdk.constant.WxsdkConstant;
import com.xyc.userc.entity.PcUser;
import com.xyc.userc.service.BlacklistService;
import com.xyc.userc.util.*;
import com.xyc.userc.vo.BlacklistVo;
import com.xyc.userc.vo.EnvInfoVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by 1 on 2020/8/24.
 */
@RestController
//@CrossOrigin
@RequestMapping("/user")
@Api(tags = "黑名单管理相关api")
public class PcBlacklistController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);

    @Resource
    BlacklistService blacklistService;

    @PostMapping("/queryBlacklist")
    @ApiOperation(value="查询黑名单")
    @ApiImplicitParams({@ApiImplicitParam(name="name", value="被拉入黑名单的人的昵称", required=false, dataType="String"),
            @ApiImplicitParam(name="mobile", value="被拉入黑名单的人的手机号", required=false, dataType="String"),
            @ApiImplicitParam(name="createName", value="黑名创建人的昵称", required=false, dataType="String"),
            @ApiImplicitParam(name="createMobile", value="黑名创建人的手机号", required=false, dataType="String"),
            @ApiImplicitParam(name="page", value="当前页码", required=true, dataType="String"),
            @ApiImplicitParam(name="size", value="每页记录条数", required=true, dataType="String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj queryBlacklist(String name, String mobile, String createName,
                                        String createMobile, String page, String size)
    {
        LOGGER.info("开始查询黑名单 name={} mobile={} createName={} createMobile={} page={} size={}",name,mobile,createName,createMobile,page,size);
        JsonResultObj_Page resultObj_Page = null;
        try
        {
            List resList = blacklistService.getBlacklist(name,mobile,createName,createMobile,page,size);
//            resultObj = new JsonResultObj(true,blacklistVoList);
            List<BlacklistVo> blacklistVos = null;
            String total = null;
            if(resList != null && resList.size() == 4)
            {
                total = resList.get(0).toString();
                page = resList.get(1).toString();
                size = resList.get(2).toString();
                blacklistVos = (List<BlacklistVo>)resList.get(3);
            }
            resultObj_Page = new JsonResultObj_Page(true,blacklistVos,total,page,size);
            LOGGER.info("查询黑名单成功");
        }
        catch (Exception e)
        {
            resultObj_Page = CommonExceptionHandler.handException_page(e, "查询黑名单失败", LOGGER, resultObj_Page);
        }
        LOGGER.info("结束查询黑名单 name={} mobile={} createName={} createMobile={} page={} size={}",name,mobile,createName,createMobile,page,size);
        return resultObj_Page;
    }

    @PostMapping("/addBlacklist")
    @ApiOperation(value="新增黑名单")
    @ApiImplicitParams({@ApiImplicitParam(name="mobile", value="被拉入黑名单的人的手机号", required=true, dataType="String"),
            @ApiImplicitParam(name="reason", value="拉黑原因", required=true, dataType="String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：新增成功 isSuccess=false：新增失败，resMsg为错误信息")})
    public JsonResultObj addBlacklist(String mobile, String reason, @ApiIgnore HttpServletRequest request)
    {
        LOGGER.info("开始新增黑名单 mobile={} reason={}", mobile,reason);
        JsonResultObj resultObj = null;
        PcUser pcUser = (PcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(pcUser == null)
        {
            LOGGER.error("用户未登陆,新增黑名单失败！mobile={}",mobile);
            resultObj = new JsonResultObj(false,JsonResultEnum.USER_NOT_LOGIN);
        }
        else
        {
            try
            {
                blacklistService.addBlacklist(mobile, reason,pcUser.getId());
                resultObj = new JsonResultObj(true);
            }
            catch (Exception e)
            {
                resultObj = CommonExceptionHandler.handException(e, "新增黑名单失败", LOGGER, resultObj);
            }
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
