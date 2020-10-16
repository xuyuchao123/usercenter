package com.xyc.userc.controller;

import com.avei.shriety.wx_sdk.constant.WxsdkConstant;
import com.xyc.userc.entity.CarNumOpenId;
import com.xyc.userc.entity.User;
import com.xyc.userc.service.CarNumService;
import com.xyc.userc.service.ShipReportService;
import com.xyc.userc.util.CommonExceptionHandler;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.JsonResultObj;
import com.xyc.userc.util.UsercConstant;
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
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 1 on 2020/9/22.
 */
@RestController
@CrossOrigin
@RequestMapping("/mes")
@Api(tags = "船户通行相关api")
public class ReportController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

    @Resource
    ShipReportService shipReportService;

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
                shipReportService.addShipReport(shipNum, name, reportDepartment, travelTimeStartDate, travelTimeEndDate, openId);
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

}
