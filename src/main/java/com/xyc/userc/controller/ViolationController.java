package com.xyc.userc.controller;

import com.xyc.userc.entity.Violation;
import com.xyc.userc.entity.ViolationDetail;
import com.xyc.userc.service.BlacklistService;
import com.xyc.userc.service.ViolationService;
import com.xyc.userc.util.CommonExceptionHandler;
import com.xyc.userc.util.JsonResultObj;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 1 on 2020/10/22.
 */
@RestController
@CrossOrigin
@RequestMapping("/mes")
@Api(tags = "违章罚款相关api")
public class ViolationController {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ViolationController.class);

    @Resource
    ViolationService violationService;

    @GetMapping("/queryViolationType")
    @ApiOperation(value = "查询违章大类")
//    @ApiImplicitParams({@ApiImplicitParam(name="name", value="被拉入黑名单的人的昵称", required=true, dataType="String"),
//            @ApiImplicitParam(name="mobile", value="被拉入黑名单的人的手机号", required=true, dataType="String"),
//            @ApiImplicitParam(name="createName", value="黑名创建人的昵称", required=true, dataType="String"),
//            @ApiImplicitParam(name="createMobile", value="黑名创建人的手机号", required=true, dataType="String")})
    @ApiResponses({@ApiResponse(code = 200, message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj queryViolation()
    {
        LOGGER.info("开始查询违章大类");
        JsonResultObj resultObj = null;
        try
        {
            List<Violation> violations = violationService.getAllViolation();
            resultObj = new JsonResultObj(true, violations);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "查询违章大类失败", LOGGER, resultObj);
        }
        LOGGER.info("结束查询违章大类");
        return resultObj;
    }

    @PostMapping("/queryViolationDetailType")
    @ApiOperation(value = "查询违章细类")
    @ApiImplicitParam(name="typeId", value="违章大类id", required=true, dataType="String")
    @ApiResponses({@ApiResponse(code = 200, message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj queryViolationDetailType(String typeId)
    {
        LOGGER.info("开始查询违章细类 typeId={}",typeId);
        JsonResultObj resultObj = null;
        try
        {
            List<ViolationDetail> violations = violationService.getViolationDetail(typeId);
            resultObj = new JsonResultObj(true, violations);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "查询违章细类失败", LOGGER, resultObj);
        }
        LOGGER.info("结束查询违章细类 typeId={}",typeId);
        return resultObj;
    }
}
