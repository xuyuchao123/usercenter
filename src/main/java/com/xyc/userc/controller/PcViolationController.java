package com.xyc.userc.controller;

import com.xyc.userc.entity.ViolationInfo;
import com.xyc.userc.service.ViolationService;
import com.xyc.userc.util.CommonExceptionHandler;
import com.xyc.userc.util.JsonResultObj;
import com.xyc.userc.util.JsonResultObj_Page;
import com.xyc.userc.vo.BindedUserRoleVo;
import com.xyc.userc.vo.DefaultUserRoleVo;
import com.xyc.userc.vo.ViolationInfoVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by 1 on 2021/4/1.
 */
@RestController
@CrossOrigin
@RequestMapping("/mock")
@Api(tags = "pc端违章查询相关api")
public class PcViolationController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(PcViolationController.class);

    @Resource
    ViolationService violationService;

    @PostMapping("/form/add")
    @ApiOperation(value="新增违章信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="billingDepartment", value="开单部门", required=true, dataType="String"),
            @ApiImplicitParam(name="billingTime", value="开单时间", required=true, dataType="String"),
            @ApiImplicitParam(name="openSingle", value="开单人", required=true, dataType="String"),
            @ApiImplicitParam(name="billingSerialNumber", value="开单序号", required=true, dataType="String"),
            @ApiImplicitParam(name="theAmountOfTheFine", value="罚款金额", required=true, dataType="String"),
            @ApiImplicitParam(name="illegalPictures", value="违章图片", required=true, dataType="String"),
            @ApiImplicitParam(name="paymentStatus", value="支付状态", required=true, dataType="String"),
            @ApiImplicitParam(name="reasonForFine", value="罚款事由", required=true, dataType="String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj addViolationInfo(@RequestParam("billingDepartment")String billDep,  @RequestParam("billingTime")String billTime,
                                          @RequestParam("openSingle")String billStaff,       @RequestParam("billingSerialNumber")String billNum,
                                          @RequestParam("theAmountOfTheFine")String fineAmt, @RequestParam("illegalPictures")String violationImgPath,
                                          @RequestParam("paymentStatus")String paymentStatus,@RequestParam("reasonForFine")String fineReason)
    {
        LOGGER.info("开始新增违章信息 billDep={} billTime={} billStaff={} billNum={} fineAmt={} violationImgPath={} paymentStatus={} fineReason={}"
                ,billDep,billTime,billStaff,billNum,fineAmt,violationImgPath,paymentStatus,fineReason);
        JsonResultObj resultObj = null;
        try
        {
            violationService.addViolationInfo(billDep,billTime,billStaff,billNum,fineAmt,violationImgPath,paymentStatus,fineReason);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException_page(e, "新增违章信息失败", LOGGER);
        }
        LOGGER.info("结束新增违章信息 billDep={} billTime={} billStaff={} billNum={} fineAmt={} violationImgPath={} paymentStatus={} fineReason={}"
                ,billDep,billTime,billStaff,billNum,fineAmt,violationImgPath,paymentStatus,fineReason);
        return resultObj;
    }


    @PostMapping("/form/query")
    @ApiOperation(value="查询违章信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="billingMethod", value="开单方式", required=false, dataType="String",defaultValue = "1：手动开单 2：自动开单"),
            @ApiImplicitParam(name="billingDepartment", value="开单部门", required=false, dataType="String"),
            @ApiImplicitParam(name="billingTime", value="开单时间", required=false, dataType="String"),
            @ApiImplicitParam(name="paymentStatus", value="支付状态", required=false, dataType="String"),
            @ApiImplicitParam(name="billingSerialNumber", value="开单序号", required=false, dataType="String"),
            @ApiImplicitParam(name="page", value="当前页码", required=true, dataType="String"),
            @ApiImplicitParam(name="size", value="每页记录条数", required=true, dataType="String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj_Page<ViolationInfoVo> queryViolationInfo(@RequestParam(value = "billingMethod",required = false)String billType,
                             @RequestParam(value = "billingDepartment",required = false)String billDep,
                             @RequestParam(value = "billingTime",required = false)String billTime, @RequestParam(value = "paymentStatus",required = false)String paymentStatus,
                             @RequestParam(value = "billingSerialNumber",required = false)String billNum,
                                                                  String page, String size)
    {
        LOGGER.info("开始查询违章信息 billType={} billDep={} billTime={} paymentStatus={} billNum={} page={} size={}"
                ,billType,billDep,billTime,paymentStatus,billNum,page,size);
        JsonResultObj_Page resultObj_page = null;
        try
        {
            List resList = violationService.getViolationInfo(billType,billDep,billTime,paymentStatus,billNum,page,size);
            List<ViolationInfoVo> violationInfoVos = null;
            String total = null;
            if(resList != null && resList.size() == 4)
            {
                total = resList.get(0).toString();
                page = resList.get(1).toString();
                size = resList.get(2).toString();
                violationInfoVos = (List<ViolationInfoVo>)resList.get(3);
            }
            resultObj_page = new JsonResultObj_Page(true,violationInfoVos,total,page,size);
        }
        catch (Exception e)
        {
            resultObj_page = CommonExceptionHandler.handException_page(e, "查询违章信息失败", LOGGER);
        }

        LOGGER.info("结束查询违章信息 billType={} billDep={} billTime={} paymentStatus={} billNum={} page={} size={}"
                ,billType,billDep,billTime,paymentStatus,billNum,page,size);
        return resultObj_page;
    }

//    @GetMapping(value = "/showViolationImg/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
//    @ApiOperation(value="显示违章图片")
//    @ApiImplicitParam(name="id", value="违章信息id", required=true, dataType="String")
//    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
//    public byte[] showViolationImg(@PathVariable("id") String id)
//    {
//        LOGGER.info("开始查询违章图片 id {}",id);
////        JsonResultObj resultObj = null;
//        try
//        {
//            byte[] bytes = violationService.getViolationImg(Integer.valueOf(id));
//            return bytes;
//        }
//        catch (Exception e)
//        {
//           CommonExceptionHandler.handException_page(e, "查询违章图片失败", LOGGER);
//        }
//        LOGGER.info("结束查询违章图片 id {}",id);
//        return null;
//    }


    @PostMapping(value = "/uploadViolationImg")
    @ApiOperation(value="上传违章图片")
    @ApiImplicitParam(name="violationImg", value="违章图片", required=true, dataType="File")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true： 上传成功 isSuccess=false：上传失败，resMsg为错误信息")})
    public JsonResultObj showViolationImg(@RequestParam("violationImg") MultipartFile violationImg)
    {
        LOGGER.info("开始上传违章图片 img {}",violationImg.getOriginalFilename());
        JsonResultObj resultObj = null;
        try
        {
            String fileNewName = violationService.uploadViolationImg(violationImg);
            resultObj = new JsonResultObj(true,fileNewName);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException_page(e, "查询违章图片失败", LOGGER);
        }
        LOGGER.info("结束上传违章图片 img {}",violationImg.getOriginalFilename());
        return resultObj;
    }

    @PostMapping("/form/remove")
    @ApiOperation(value="删除违章信息")
    @ApiImplicitParam(name="billingSerialNumber", value="开单序号", required=false, dataType="String")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：删除成功 isSuccess=false：删除失败，resMsg为错误信息")})
    public JsonResultObj removeViolationInfo(@RequestParam("billingSerialNumber")String billNum)
    {
        LOGGER.info("开始删除违章信息 billNum={}",billNum);
        JsonResultObj resultObj = null;
        try
        {
            violationService.removeViolationInfo(billNum);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException_page(e, "删除违章信息失败", LOGGER);
        }

        LOGGER.info("结束删除违章信息 billNum={}",billNum);
        return resultObj;
    }


}
