package com.xyc.userc.controller;

import com.xyc.userc.service.HallReportService;
import com.xyc.userc.util.CommonExceptionHandler;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.JsonResultObj;
import com.xyc.userc.util.UsercConstant;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by 1 on 2021/2/1.
 */
@RestController
@CrossOrigin
@RequestMapping("/mes")
@Api(tags = "久隆物流大厅报道相关api")
public class HallReportController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(HallReportController.class);

    @Resource
    HallReportService hallReportService;

    @PostMapping("/addReportInfo")
    @ApiOperation(value="新增物流大厅报道记录")
    @ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "carNum", value = "车牌号", required = true, dataType = "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：新增成功 isSuccess=false：新增失败，resMsg为错误信息")})
    public JsonResultObj addReportInfo(@ApiIgnore HttpServletRequest request, String mobile, String carNum)
    {
        LOGGER.info("开始新增物流大厅报道记录 mobile={} carNum={}",mobile,mobile);
        JsonResultObj resultObj = null;
        String openId = request.getHeader(UsercConstant.OPENID);
        if(openId == null)
        {
            LOGGER.info("未获取到用户的openId");
            resultObj = new JsonResultObj(false, JsonResultEnum.OPENID_NOT_EXIST);
        }
        else if(openId.equals("undefined"))
        {
            LOGGER.info("openId为 undefined");
            resultObj = new JsonResultObj(false,JsonResultEnum.OPENID_UNDEFINED);
        }
        else
        {
            LOGGER.info("openId={}",openId);
            try
            {
                int id = hallReportService.addReportInfo(openId,mobile,carNum);
                resultObj = new JsonResultObj(true,id);
            }
            catch (Exception e)
            {
                resultObj = CommonExceptionHandler.handException(e, "新增物流大厅报道记录失败", LOGGER, resultObj);
            }
        }

        LOGGER.info("结束新增物流大厅报道记录 mobile={} carNum={}",mobile,mobile);
        return resultObj;
    }

    @PostMapping("/queryWaitingNum")
    @ApiOperation(value="查询等待人数")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj queryWaitingNum(@ApiIgnore HttpServletRequest request)
    {
        LOGGER.info("开始查询等待人数");
        JsonResultObj resultObj = null;
        String openId = request.getHeader(UsercConstant.OPENID);
        if(openId == null)
        {
            LOGGER.info("未获取到用户的openId");
            resultObj = new JsonResultObj(false, JsonResultEnum.OPENID_NOT_EXIST);
        }
        else if(openId.equals("undefined"))
        {
            LOGGER.info("openId为 undefined");
            resultObj = new JsonResultObj(false,JsonResultEnum.OPENID_UNDEFINED);
        }
        else
        {
            LOGGER.info("openId={}",openId);
            try
            {
                int waitingNum = hallReportService.getWaitingNum(openId);
                resultObj = new JsonResultObj(true,waitingNum);
            }
            catch (Exception e)
            {
                resultObj = CommonExceptionHandler.handException(e, "查询等待人数失败", LOGGER, resultObj);
            }
        }

        LOGGER.info("结束查询等待人数");
        return resultObj;
    }

    @GetMapping("/queryCurrentNum")
    @ApiOperation(value="查询当前被叫到的序号")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj queryCurrentNum(@ApiIgnore HttpServletRequest request)
    {
        LOGGER.info("开始查询当前被叫到的序号");
        JsonResultObj resultObj = null;
        try
        {
            int curNum = hallReportService.getCurrentNum();
            resultObj = new JsonResultObj(true,curNum);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "查询当前被叫到的序号失败", LOGGER, resultObj);
        }

        LOGGER.info("结束查询当前被叫到的序号");
        return resultObj;
    }
}
