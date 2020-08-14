package com.xyc.userc.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xyc.userc.entity.Application;
import com.xyc.userc.service.ApplicationService;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.JsonResultObj;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 1 on 2020/7/2.
 * 应用列表控制器
 */

@Controller
@RequestMapping("/application")
@Api(tags = "已注册系统列表相关api")
public class ApplicationController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

    @Resource
    ApplicationService applicationService;

    @RequestMapping(value = "/queryAllApplication",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="查询所有已注册系统列表")
    @ApiResponses(
            {@ApiResponse(code = 200, message = "isSuccess=true：查询成功，resDat返回系统对象列表 isSuccess=false：查询失败，resMsg为错误信息，resData为空")})
    public JsonResultObj queryAllApplication()
    {
        LOGGER.info("开始查询已注册系统列表");
        JsonResultObj resultObj = null;
        try
        {
            PageHelper.startPage(1,10);
            List<Application> applicationList = applicationService.queryAllApplications();
            Page applicationListPage = (Page)applicationList;
            if(applicationList == null || applicationList.size() == 0)
            {
//                System.out.println((JSONObject.toJSONString(applicationList)));
                applicationListPage.setPages(1);
            }
            resultObj = new JsonResultObj(true,applicationList);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("查询已注册系统列表失败：{}",e.getMessage());
            resultObj = new JsonResultObj(false);
        }
        LOGGER.info("结束查询已注册系统列表");
        return resultObj;
    }

    @RequestMapping(value = "/queryApplication",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "查询已注册系统列表", notes = "根据条件查询已注册系统列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "appId", value = "系统appId", required = false, dataType = "String"),
            @ApiImplicitParam(name = "appName", value = "系统名称", required = false, dataType = "String"),
            @ApiImplicitParam(name = "createUserNum", value = "创建人工号", required = false, dataType = "String"),
            @ApiImplicitParam(name = "createUsername", value = "创建人姓名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "要显示第几页的数据", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", required = false, dataType = "String")})
    @ApiResponses(
            {@ApiResponse(code = 200, message = "isSuccess=true：查询成功，resData返回系统对象列表 isSuccess=false：查询失败，resMsg为错误信息，resData为空")})
    public JsonResultObj queryApplication(String appId, String appName, String createUserNum,
                                   String createUsername, String pageNum, String pageSize)
    {
        LOGGER.info("开始条件查询已注册系统列表");
        JsonResultObj resultObj = null;
        try
        {
            int pNum = Integer.valueOf(pageNum);
            int pSize = Integer.valueOf(pageSize);
            PageHelper.startPage(pNum,pSize);

            List<Application> applicationList = applicationService.queryApplication(appId,appName,
                                                    createUserNum,createUsername);
            Page applicationListPage = (Page)applicationList;
            if(applicationList == null || applicationList.size() == 0)
            {
                applicationListPage.setPages(1);
            }
            resultObj = new JsonResultObj(true,applicationList);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("条件查询已注册系统列表失败：{}",e.getMessage());
            resultObj = new JsonResultObj(false);
        }
        LOGGER.info("结束条件查询已注册系统列表");
        return resultObj;
    }


}
