package com.xyc.userc.controller;

import com.xyc.userc.entity.QRCodeStrInfo;
import com.xyc.userc.service.HallReportService;
import com.xyc.userc.util.CommonExceptionHandler;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.JsonResultObj;
import com.xyc.userc.util.UsercConstant;
import com.xyc.userc.vo.HallReportCommentVo;
import com.xyc.userc.vo.HallReportInfoVo;
import com.xyc.userc.vo.HallReportPrintQueueVo;
import com.xyc.userc.vo.QRCodeStrVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/addHallReportInfo")
    @ApiOperation(value="新增物流大厅报道记录")
    @ApiImplicitParams({@ApiImplicitParam(name = "bigLadingBillNo", value = "提单号", required = false, dataType = "String"),
            @ApiImplicitParam(name = "qrCodeStr", value = "二维码", required = true, dataType = "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：新增成功 isSuccess=false：新增失败，resMsg为错误信息")})
    public JsonResultObj addHallReportInfo(@ApiIgnore HttpServletRequest request,String bigLadingBillNo, String qrCodeStr)
    {
        LOGGER.info("开始新增物流大厅报道记录 bigLadingBillNo={} qrCodeStr={}",bigLadingBillNo,qrCodeStr);
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
                List<String> list = hallReportService.addReportInfo(openId,bigLadingBillNo,qrCodeStr);
                if(list.size() == 1)
                {
                    resultObj = new JsonResultObj(true,list.get(0));
                }
                else
                {
                    resultObj = new JsonResultObj(false,list);
                }

            }
            catch (Exception e)
            {
                resultObj = CommonExceptionHandler.handException(e, "新增物流大厅报道记录失败", LOGGER);
            }
        }

        LOGGER.info("结束新增物流大厅报道记录 bigLadingBillNo={} qrCodeStr={}",bigLadingBillNo,qrCodeStr);
        return resultObj;
    }

    @GetMapping("/queryWaitingNum")
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
                resultObj = CommonExceptionHandler.handException(e, "查询等待人数失败", LOGGER);
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
            List<Integer> curNums = hallReportService.getCurrentNum();
            resultObj = new JsonResultObj(true,curNums);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "查询当前被叫到的序号失败", LOGGER);
        }

        LOGGER.info("结束查询当前被叫到的序号");
        return resultObj;
    }

    @GetMapping("/queryHallReportInfo")
    @ApiOperation(value="查询大厅报道信息")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj<HallReportInfoVo> queryHallReportInfo(@ApiIgnore HttpServletRequest request)
    {
        LOGGER.info("开始查询大厅报道信息");
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
            LOGGER.info("openId={}", openId);
            try
            {
                HallReportInfoVo hallReportInfoVo = hallReportService.getHallReportInfo(openId);
                resultObj = new JsonResultObj(true, hallReportInfoVo);
            }
            catch (Exception e)
            {
                resultObj = CommonExceptionHandler.handException(e, "查询大厅报道信息失败", LOGGER);
            }
        }

        LOGGER.info("结束查询大厅报道信息");
        return resultObj;
    }

    @PostMapping("/addHallReportComment")
    @ApiOperation(value="新增大厅报道评论")
    @ApiImplicitParams({@ApiImplicitParam(name = "carNum", value = "车牌号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "comment", value = "评论内容", required = true, dataType = "String"),
            @ApiImplicitParam(name = "bigLadingBillNo", value = "提单号", required = true, dataType = "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：新增成功 isSuccess=false：新增失败，resMsg为错误信息")})
    public JsonResultObj<HallReportCommentVo> addHallReportComment(@ApiIgnore HttpServletRequest request, String carNum, String comment, String bigLadingBillNo)
    {
        LOGGER.info("开始新增大厅报道评论 carNum={},bigLadingBillNo={}",carNum,bigLadingBillNo);
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
            LOGGER.info("openId={}", openId);
            try
            {
                HallReportCommentVo hallReportCommentVo = hallReportService.addHallReportComment(openId,carNum,comment,bigLadingBillNo);
                resultObj = new JsonResultObj(true,hallReportCommentVo);
            }
            catch (Exception e)
            {
                resultObj = CommonExceptionHandler.handException(e, "新增大厅报道评论失败", LOGGER);
            }
        }

        LOGGER.info("结束新增大厅报道评论 carNum={},bigLadingBillNo={}",carNum,bigLadingBillNo);
        return resultObj;
    }

    @GetMapping("/getHallReportQRCodeStr")
    @ApiOperation(value="获取大厅报道二维码字符串")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：获取成功 isSuccess=false：获取失败，resMsg为错误信息")})
    public JsonResultObj<QRCodeStrVo> getHallReportQRCodeStr()
    {
        LOGGER.info("开始获取大厅报道二维码字符串");
        JsonResultObj resultObj = null;
        try
        {
            QRCodeStrVo qrCodeStrVo = hallReportService.getQRCodeStr();
            resultObj = new JsonResultObj(true,qrCodeStrVo);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "获取大厅报道二维码字符串失败", LOGGER);
        }
        LOGGER.info("结束获取大厅报道二维码字符串");
        return resultObj;
    }

    @GetMapping("/getHallReportQueue")
    @ApiOperation(value="获取大厅报道及打印队列")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：获取成功 isSuccess=false：获取失败，resMsg为错误信息")})
    public JsonResultObj<HallReportPrintQueueVo> getHallReportQueue()
    {
        LOGGER.info("开始获取大厅报道队列");
        JsonResultObj resultObj = null;
        try
        {
            Map<String,List<HallReportPrintQueueVo>> stringListMap = hallReportService.getReportQueue();
            resultObj = new JsonResultObj(true,stringListMap);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "获取获取大厅报道队列失败", LOGGER);
        }
        LOGGER.info("结束获取大厅报道队列");
        return resultObj;
    }


}
