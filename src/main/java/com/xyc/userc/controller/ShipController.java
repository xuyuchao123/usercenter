package com.xyc.userc.controller;

import com.xyc.userc.entity.CarNumOpenId;
import com.xyc.userc.service.ShipService;
import com.xyc.userc.util.CommonExceptionHandler;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.JsonResultObj;
import com.xyc.userc.util.UsercConstant;
import com.xyc.userc.vo.ShipInfoVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 1 on 2020/11/17.
 */
@RestController
@CrossOrigin
@RequestMapping("/mes")
@Api(tags = "船户通行相关api")
public class ShipController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(ShipController.class);

    @Resource
    ShipService shipService;


    @PostMapping("/addShipReport")
    @ApiOperation(value="新增船户报港数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "shipNum", value = "船号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "姓名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "reportDepartment", value = "报港部门", required = true, dataType = "String"),
            @ApiImplicitParam(name = "travelTimeStart", value = "出行开始时间", required = true, dataType = "String"),
            @ApiImplicitParam(name = "travelTimeEnd", value = "出行结束时间", required = true, dataType = "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：新增成功 isSuccess=false：新增失败，resMsg为错误信息")})
    public JsonResultObj addShipReport(String shipNum, String name, String reportDepartment,
                                       String travelTimeStart, String travelTimeEnd, @ApiIgnore HttpServletRequest request)
    {
        LOGGER.info("开始新增船户报港数据 shipNum={},name={},reportDepartment={},travelTimeStart={},travelTimeEnd={}",
                shipNum,name,reportDepartment,travelTimeStart,travelTimeEnd);
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
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(travelTimeStart));
                Date travelTimeStartDate = calendar.getTime();
                calendar.setTimeInMillis(Long.valueOf(travelTimeEnd));
                Date travelTimeEndDate = calendar.getTime();
                shipService.addShipReport(shipNum, name, reportDepartment, travelTimeStartDate, travelTimeEndDate, openId);
                resultObj = new JsonResultObj(true);
            }
            catch (Exception e)
            {
                resultObj = CommonExceptionHandler.handException(e, "新增船户报港数据失败", LOGGER, resultObj);
            }
        }
        LOGGER.info("结束新增船户报港数据");
        return resultObj;
    }



    @PostMapping("/addShipInfo-hl")
    @ApiOperation(value="保存海力物流船户信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "shipNum", value = "船舶号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "船民电话", required = true, dataType = "String"),
            @ApiImplicitParam(name = "cargoName", value = "货名", required = true, dataType = "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：保存成功 isSuccess=false：保存失败，resMsg为错误信息")})
    public JsonResultObj addShipInfo_hl(String shipNum, String mobile, String cargoName)
    {
        LOGGER.info("开始审核并保存海力物流船户信息: shipNum:{} mobile:{} cargoName:{}",shipNum,mobile,cargoName);
        JsonResultObj resultObj = null;
        try
        {
            int id = shipService.checkAndAddShipInfo(shipNum,mobile,cargoName);
            resultObj = new JsonResultObj(true, id);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "审核并保存海力物流船户信息失败", LOGGER, resultObj);
        }
        LOGGER.info("结束审核并保存海力物流船户信息: shipNum:{} mobile:{} cargoName:{}",shipNum,mobile,cargoName);
        return resultObj;
    }

    @PostMapping("/addShipInfo-fg")
    @ApiOperation(value="保存废钢处船户信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "shipNum", value = "船舶号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "船民电话", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "姓名", required = true, dataType = "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：保存成功 isSuccess=false：保存失败，resMsg为错误信息")})
    public JsonResultObj addShipInfo_fg(String shipNum, String mobile, String name)
    {
        LOGGER.info("开始保存废钢处船户信息: shipNum:{} mobile:{} name:{}",shipNum,mobile,name);
        JsonResultObj resultObj = null;
        try
        {
            int id = shipService.addShipInfo(shipNum,mobile,"",name);
            resultObj = new JsonResultObj(true, id);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "保存废钢处船户信息失败", LOGGER, resultObj);
        }
        LOGGER.info("结束保存废钢处船户信息: shipNum:{} mobile:{} name:{}",shipNum,mobile,name);
        return resultObj;
    }

    @PostMapping("/queryShipInfo")
    @ApiOperation(value="查询船户信息")
    @ApiImplicitParam(name = "", value = "id号", required = true, dataType = "String")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：保存成功 isSuccess=false：保存失败，resMsg为错误信息")})
    public JsonResultObj<ShipInfoVo> queryShipInfo(String id)
    {
        LOGGER.info("开始查询船户信息 id:{}",id);
        JsonResultObj resultObj = null;
        try
        {
            List<ShipInfoVo> shipInfoVos = shipService.queryShipInfo(id);
            resultObj = new JsonResultObj(true, shipInfoVos);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "查询船户信息失败", LOGGER, resultObj);
        }
        LOGGER.info("结束查询船户信息 id:{}",id);
        return resultObj;
    }
}
