package com.xyc.userc.controller;

import com.xyc.userc.service.GarageService;
import com.xyc.userc.util.CommonExceptionHandler;
import com.xyc.userc.util.JsonResultObj;
import com.xyc.userc.vo.GarageInfoVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 1 on 2021/1/26.
 */
@RestController
@CrossOrigin
@RequestMapping("/mes")
@Api(tags = "库位信息管理相关api")
public class GarageController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(GarageController.class);

    @Resource
    GarageService garageService;

    @PostMapping("/queryGarageInfo")
    @ApiOperation(value="查询库位配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "garageType", value = "库位类型", required = true, dataType = "String"),
            @ApiImplicitParam(name = "garageNum", value = "库位编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "garageName", value = "库位名称", required = true, dataType = "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj queryGarageInfo(String garageType,String garageNum,String garageName)
    {
        LOGGER.info("开始查询库位配置信息 garageType={} garageNum={} garageName={}",garageType,garageNum,garageName);
        JsonResultObj resultObj = null;
        try
        {
            List<GarageInfoVo> garageInfoVoList = garageService.getGarageInfo(garageType,garageNum,garageName);
            resultObj = new JsonResultObj(true, garageInfoVoList);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "查询库位配置信息失败", LOGGER, resultObj);
        }
        LOGGER.info("结束查询库位配置信息 garageType={} garageNum={} garageName={}",garageType,garageNum,garageName);
        return resultObj;
    }

    @PostMapping("/addGarageInfo")
    @ApiOperation(value="新增库位配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "garageType", value = "库位类型", required = true, dataType = "String"),
            @ApiImplicitParam(name = "garageNum", value = "库位编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "garageName", value = "库位名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "maxLimit", value = "最大限制数量", required = true, dataType = "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：新增成功 isSuccess=false：新增失败，resMsg为错误信息")})
    public JsonResultObj addGarageInfo(String garageType,String garageNum,String garageName,String maxLimit)
    {
        LOGGER.info("开始新增库位配置信息 garageType={} garageNum={} garageName={} maxLimit={}",garageType,garageNum,garageName,maxLimit);
        JsonResultObj resultObj = null;
        try
        {
            garageService.addGarageInfo(garageType,garageNum,garageName,maxLimit);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "新增库位配置信息失败", LOGGER, resultObj);
        }
        LOGGER.info("结束新增库位配置信息 garageType={} garageNum={} garageName={} maxLimit={}",garageType,garageNum,garageName,maxLimit);
        return resultObj;
    }

    @PostMapping("/updateGarageInfo")
    @ApiOperation(value="修改库位配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "garageNum", value = "库位编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "maxLimit", value = "最大限制数量", required = true, dataType = "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：修改成功 isSuccess=false：修改失败，resMsg为错误信息")})
    public JsonResultObj updateGarageInfo(String garageNum,String maxLimit)
    {
        LOGGER.info("开始修改库位配置信息 garageNum={} maxLimit={}",garageNum,maxLimit);
        JsonResultObj resultObj = null;
        try
        {
            garageService.modifyGarageInfo(garageNum,maxLimit);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "修改库位配置信息失败", LOGGER, resultObj);
        }
        LOGGER.info("结束修改库位配置信息 garageNum={} maxLimit={}",garageNum,maxLimit);
        return resultObj;
    }

    @PostMapping("/deleteGarageInfo")
    @ApiOperation(value="删除库位配置信息")
    @ApiImplicitParam(name = "garageNum", value = "库位编码", required = true, dataType = "String")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：删除成功 isSuccess=false：删除失败，resMsg为错误信息")})
    public JsonResultObj deleteGarageInfo(String garageNum)
    {
        LOGGER.info("开始删除库位配置信息 garageNum={}",garageNum);
        JsonResultObj resultObj = null;
        try
        {
            garageService.removeGarageInfo(garageNum);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "删除库位配置信息失败", LOGGER, resultObj);
        }
        LOGGER.info("结束删除库位配置信息 garageNum={}",garageNum);
        return resultObj;
    }

    @PostMapping("/queryMaxLimit")
    @ApiOperation(value="查询库位最大限制量")
    @ApiImplicitParam(name = "garageType", value = "库位类型", required = true, dataType = "String")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj queryMaxLimit(String garageNum)
    {
        LOGGER.info("开始查询库位最大限制量 garageNum={}",garageNum);
        JsonResultObj resultObj = null;
        try
        {
            int sumMaxLimit = garageService.getMaxLimit(garageNum);
            resultObj = new JsonResultObj(true,sumMaxLimit);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "查询库位最大限制量失败", LOGGER, resultObj);
        }
        LOGGER.info("结束查询库位最大限制量 garageNum={}",garageNum);
        return resultObj;
    }
}
