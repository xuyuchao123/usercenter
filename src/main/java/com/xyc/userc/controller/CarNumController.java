package com.xyc.userc.controller;

import com.avei.shriety.wx_sdk.constant.WxsdkConstant;
import com.xyc.userc.entity.CarNumOpenId;
import com.xyc.userc.entity.User;
import com.xyc.userc.service.CarNumService;
import com.xyc.userc.util.*;
import com.xyc.userc.vo.CarNumInOutTimeVo;
import com.xyc.userc.vo.EnvInfoVo;
import com.xyc.userc.vo.GsCarInfoVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 1 on 2020/8/24.
 */
@RestController
@CrossOrigin
@RequestMapping("/mes")
@Api(tags = "车牌号管理相关api")
public class CarNumController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);

    @Resource
    CarNumService carNumService;


    @PostMapping("/queryCarNum")
    @ApiOperation(value="查询车牌号")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj queryCarNum(@ApiIgnore HttpServletRequest request)
    {
        LOGGER.info("开始查询车牌号");
        JsonResultObj resultObj = null;
        String openId = request.getHeader(UsercConstant.OPENID);
        if(openId == null)
        {
            LOGGER.info("未获取到用户的openId");
            resultObj = new JsonResultObj(false, JsonResultEnum.OPENID_NOT_EXIST);
        }
        else
        {
            try
            {
                LOGGER.info("获取到用户openId={}", openId);
                List<CarNumOpenId> carNumOpenIdList = carNumService.getCarNum(openId);
                resultObj = new JsonResultObj(true, carNumOpenIdList);
            }
            catch (Exception e)
            {
                resultObj = CommonExceptionHandler.handException(e, "查询车牌号失败", LOGGER, resultObj);
            }
        }
        LOGGER.info("结束查询车牌号");
        return resultObj;
    }

    @PostMapping("/deleteCarNum")
    @ApiOperation(value="删除车牌号")
    @ApiImplicitParam(name = "carNum", value = "车牌号", required = true, dataType = "String")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：删除成功 isSuccess=false：删除失败，resMsg为错误信息")})
    public JsonResultObj deleteCarNum(String carNum, @ApiIgnore HttpServletRequest request)
    {
        LOGGER.info("开始删除车牌号 carNum={}",carNum);
        JsonResultObj resultObj = null;
        String openId = request.getHeader(UsercConstant.OPENID);
        if(openId == null)
        {
            LOGGER.info("未获取到用户的openId");
            resultObj = new JsonResultObj(false, JsonResultEnum.OPENID_NOT_EXIST);
        }
        else
        {
            try
            {
                carNumService.removeCarNum(carNum, openId);
                resultObj = new JsonResultObj(true);
            }
            catch (Exception e)
            {
                resultObj = CommonExceptionHandler.handException(e, "删除车牌号失败", LOGGER, resultObj);
            }
        }
        LOGGER.info("结束删除车牌号");
        return resultObj;
    }

    @PostMapping("/addCarNum")
    @ApiOperation(value="新增车牌号")
    @ApiImplicitParams({@ApiImplicitParam(name = "carNum", value = "车牌号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "engineNum", value = "发动机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "identNum", value = "车辆识别号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "emissionStd", value = "排放标准", required = true, dataType = "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：新增成功 isSuccess=false：新增失败，resMsg为错误信息")})
    public JsonResultObj addCarNum(String carNum, String engineNum, String identNum,String emissionStd, @ApiIgnore HttpServletRequest request)
    {
        LOGGER.info("开始新增车牌号 carNum={} engineNum={} identNum={} emissionStd={}",carNum,engineNum,identNum,emissionStd);
        JsonResultObj resultObj = null;
        String openId = request.getHeader(UsercConstant.OPENID);
        if(openId == null)
        {
            LOGGER.info("未获取到用户的openId");
            resultObj = new JsonResultObj(false, JsonResultEnum.OPENID_NOT_EXIST);
        }
        else
        {
            try
            {
                carNumService.addCarNum(carNum, openId, engineNum, identNum, emissionStd);
                resultObj = new JsonResultObj(true);
            }
            catch (Exception e)
            {
                resultObj = CommonExceptionHandler.handException(e, "新增车牌号失败", LOGGER, resultObj);
            }
        }
        LOGGER.info("结束新增车牌号 carNum={} engineNum={} identNum={} emissionStd={}",carNum,engineNum,identNum,emissionStd);
        return resultObj;
    }

    @PostMapping("/updateCarNum")
    @ApiOperation(value="修改车牌号")
    @ApiImplicitParams({@ApiImplicitParam(name="oldCarNum", value="原来的车牌号", required=true, dataType="String"),
            @ApiImplicitParam(name="newCarNum", value="新的车牌号", required=true, dataType="String"),
            @ApiImplicitParam(name = "engineNum", value = "发动机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "identNum", value = "车辆识别号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "emissionStd", value = "排放标准", required = true, dataType = "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：修改成功 isSuccess=false：修改失败，resMsg为错误信息")})
    public JsonResultObj updateCarNum(String oldCarNum, String newCarNum, String engineNum, String identNum,
                                      String emissionStd, @ApiIgnore HttpServletRequest request) {
        LOGGER.info("开始修改车牌号 oldCarNum={} newCarNum={} engineNum={} identNum={} emissionStd={}",
                oldCarNum, newCarNum,engineNum,identNum,emissionStd);
        JsonResultObj resultObj = null;
        String openId = request.getHeader(UsercConstant.OPENID);
        if(openId == null)
        {
            LOGGER.info("未获取到用户的openId");
            resultObj = new JsonResultObj(false, JsonResultEnum.OPENID_NOT_EXIST);
        }
        else
        {
            try
            {
                carNumService.modifyCarNumByOpenId(oldCarNum, newCarNum, engineNum, identNum, emissionStd, openId);
                resultObj = new JsonResultObj(true);
            }
            catch (Exception e)
            {
                resultObj = CommonExceptionHandler.handException(e, "修改车牌号失败", LOGGER, resultObj);
            }
        }
        LOGGER.info("结束修改车牌号 oldCarNum={} newCarNum={} engineNum={} identNum={} emissionStd={}",
                oldCarNum, newCarNum,engineNum,identNum,emissionStd);
        return resultObj;
    }

    @PostMapping("/enableCarNum")
    @ApiOperation(value="启用车牌号")
    @ApiImplicitParam(name = "carNum", value = "车牌号", required = false, dataType = "String")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：启用成功 isSuccess=false：启用失败，resMsg为错误信息")})
    public JsonResultObj enableCarNum(String carNum, @ApiIgnore HttpServletRequest request)
    {
        LOGGER.info("开始启用车牌号 carNum={}",carNum);
        JsonResultObj resultObj = null;
        String openId = request.getHeader(UsercConstant.OPENID);
        if(openId == null)
        {
            LOGGER.info("未获取到用户的openId");
            resultObj = new JsonResultObj(false, JsonResultEnum.OPENID_NOT_EXIST);
        }
        else
        {
            try
            {
                carNumService.enableCarNum(carNum, openId);
                resultObj = new JsonResultObj(true);
            }
            catch (Exception e)
            {
                resultObj = CommonExceptionHandler.handException(e, "启用车牌号失败", LOGGER, resultObj);
            }
        }
        LOGGER.info("结束启用车牌号");
        return resultObj;
    }


    @GetMapping("/queryInOutTime")
    @ApiOperation(value="查询车辆本月及上月进出厂时间")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj enableCarNum(@ApiIgnore HttpServletRequest request)
    {
        LOGGER.info("开始查询车辆进出厂时间");
        JsonResultObj resultObj = null;
        String openId = request.getHeader(UsercConstant.OPENID);
        if(openId == null)
        {
            LOGGER.info("未获取到用户的openId");
            resultObj = new JsonResultObj(false, JsonResultEnum.OPENID_NOT_EXIST);
        }
        else
        {
            try
            {
                List<CarNumInOutTimeVo> carNumInOutTimeVos = carNumService.queryInOutTime(openId);
                resultObj = new JsonResultObj(true,carNumInOutTimeVos);
            }
            catch (Exception e)
            {
                resultObj = CommonExceptionHandler.handException(e, "查询车辆进出厂时间失败", LOGGER, resultObj);
            }
        }
        LOGGER.info("结束查询车辆进出厂时间");
        return resultObj;
    }

    @PostMapping("/queryGsCarInfo")
    @ApiOperation(value="查询国三车辆识别号及发动机号")
    @ApiImplicitParam(name = "carNum", value = "车牌号", required = false, dataType = "String")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj queryGsCarInfo(String carNum)
    {
        LOGGER.info("开始查询国三车辆识别号及发动机号 carNum={}", carNum);
        JsonResultObj resultObj = null;
        try
        {
            List<GsCarInfoVo> gsCarInfoVos = carNumService.queryGsCarInfo(carNum);
            resultObj = new JsonResultObj(true,gsCarInfoVos);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "查询国三车辆识别号及发动机号失败", LOGGER, resultObj);
        }
        LOGGER.info("结束查询国三车辆识别号及发动机号 carNum={}", carNum);
        return resultObj;
    }

    @PostMapping("/queryEnvInfo")
    @ApiOperation(value="查询环保管控信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "carNum", value = "车牌号", required = false, dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "查询日期", required = false, dataType = "String"),
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页显示页数", required = true, dataType = "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj_Page queryEnvInfo(String carNum, String startDate, String page, String size)
    {
        LOGGER.info("开始查询环保管控信息 carNum={},startDate={},page={},size={}",carNum,startDate,page,size);
        JsonResultObj_Page resultObj_Page = null;
        try
        {
            List<EnvInfoVo> envInfoVos = carNumService.queryEnvInfo(carNum,startDate,page,size);
//            String total = resList.get(0).toString();
//            List<EnvInfoVo> envInfoVos = (List<EnvInfoVo>)resList.get(1);
            resultObj_Page = new JsonResultObj_Page(true,envInfoVos,page,size);
        }
        catch (Exception e)
        {
            resultObj_Page = CommonExceptionHandler.handException_page(e, "查询环保管控信息失败", LOGGER, resultObj_Page);
        }
        LOGGER.info("结束查询环保管控信息 carNum={},startDate={},page={},size={}",carNum,startDate,page,size);
        return resultObj_Page;
    }
}
