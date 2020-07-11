package com.xyc.userc.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xyc.userc.entity.Application;
import com.xyc.userc.service.ApplicationService;
import com.xyc.userc.util.JsonResultObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 1 on 2020/7/2.
 * 应用列表控制器
 */

@Controller
@RequestMapping("/application")
@Api("注册系统列表相关的api")
public class ApplicationController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

    @Resource
    ApplicationService applicationService;

    @RequestMapping(value = "/queryAllApplication",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="查询所有已注册系统列表")
    public Object queryAllApplication()
    {
        LOGGER.debug("开始查询已注册系统列表");
        Object jsonObj = null;
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
            JsonResultObj resultObj = new JsonResultObj(true,applicationList);
            jsonObj = JSONObject.toJSON(resultObj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("查询已注册系统列表失败：{}",e.getMessage());
            JsonResultObj resultObj = new JsonResultObj(false);
            jsonObj = JSONObject.toJSON(resultObj);
        }
        LOGGER.debug("结束查询已注册系统列表");
        return jsonObj;
    }

    @RequestMapping(value = "/queryApplication",method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParam(name = "appId", value = "应用appId",  paramType = "path", required = true, dataType =  "Integer")
    public Object queryApplication(String appId, String appName, String createUserNum,
                                   String createUsername, String pageNum, String pageSize)
    {
        LOGGER.debug("开始条件查询已注册系统列表");
        Object jsonObj = null;
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
            JsonResultObj resultObj = new JsonResultObj(true,applicationList);
            jsonObj = JSONObject.toJSON(resultObj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("条件查询已注册系统列表失败：{}",e.getMessage());
            JsonResultObj resultObj = new JsonResultObj(false);
            jsonObj = JSONObject.toJSON(resultObj);
        }
        LOGGER.debug("结束条件查询已注册系统列表");
        return jsonObj;
    }


}
