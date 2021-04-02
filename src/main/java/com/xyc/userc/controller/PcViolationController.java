package com.xyc.userc.controller;

import com.xyc.userc.entity.ViolationInfo;
import com.xyc.userc.service.ViolationService;
import com.xyc.userc.util.CommonExceptionHandler;
import com.xyc.userc.util.JsonResultObj;
import com.xyc.userc.util.JsonResultObj_Page;
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
@RequestMapping("/violation")
@Api(tags = "pc端违章查询相关api")
public class PcViolationController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(PcViolationController.class);

    @Resource
    ViolationService violationService;

    @PostMapping("/addViolationInfo")
    @ApiOperation(value="新增违章信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="billDep", value="开单部门", required=true, dataType="String"),
            @ApiImplicitParam(name="billTime", value="开单时间", required=true, dataType="String"),
            @ApiImplicitParam(name="billStaff", value="开单人", required=true, dataType="String"),
            @ApiImplicitParam(name="billNum", value="开单序号", required=true, dataType="String"),
            @ApiImplicitParam(name="carNumber", value="车牌号", required=true, dataType="String"),
            @ApiImplicitParam(name="fineReason", value="罚款事由", required=true, dataType="String"),
            @ApiImplicitParam(name="fineAmt", value="罚款金额", required=true, dataType="String"),
            @ApiImplicitParam(name="violationImg", value="违章图片", required=true, dataType="File"),
            @ApiImplicitParam(name="paymentStatus", value="支付状态", required=true, dataType="String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj addViolationInfo(String billDep,String billTime,String billStaff,String billNum,String carNumber,
                                                   String fineReason,String fineAmt, @RequestParam("violationImg") MultipartFile violationImg,
                                                   String paymentStatus)
    {
        LOGGER.info("开始新增违章信息 billDep={} billTime={} billStaff={} billNum={} carNumber={} fineReason={} fineAmt={} paymentStatus={}"
                ,billDep,billTime,billStaff,billNum,carNumber,fineReason,fineAmt,paymentStatus);
        JsonResultObj resultObj = null;
        try
        {
            Date date = new Date();
            ViolationInfo violationInfo = new ViolationInfo(null,billDep,new Date(Long.valueOf(billTime)),billStaff,billNum,carNumber,fineReason,fineAmt,null,
                    paymentStatus,date,1,date,1,1);
            violationService.addViolationInfo(violationInfo,violationImg);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException_page(e, "新增违章信息失败", LOGGER);
        }
        LOGGER.info("结束新增违章信息 billDep={} billTime={} billStaff={} billNum={} carNumber={} fineReason={} fineAmt={} paymentStatus={}"
                ,billDep,billTime,billStaff,billNum,carNumber,fineReason,fineAmt,paymentStatus);
        return resultObj;
    }


    @PostMapping("/queryViolationInfo")
    @ApiOperation(value="查询违章信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="billStaff", value="开单方式", required=false, dataType="String"),
            @ApiImplicitParam(name="billDep", value="开单部门", required=true, dataType="String"),
            @ApiImplicitParam(name="billTime", value="开单时间", required=true, dataType="String"),
            @ApiImplicitParam(name="carNumber", value="车牌号", required=true, dataType="String"),
            @ApiImplicitParam(name="paymentStatus", value="支付状态", required=true, dataType="String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj<ViolationInfoVo> queryViolationInfo(String billStaff, String billDep, String billTime, String carNumber, String paymentStatus)
    {
        LOGGER.info("开始查询违章信息 billStaff={} billDep={} billTime={} carNumber={} paymentStatus={}"
                ,billStaff,billDep,billTime,carNumber,paymentStatus);
        JsonResultObj resultObj = null;
        try
        {
            List<ViolationInfoVo> violationInfoVos = violationService.getViolationInfo(billStaff,billDep,billTime,carNumber,paymentStatus);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException_page(e, "新增违章信息失败", LOGGER);
        }

        LOGGER.info("结束查询违章信息 billStaff={} billDep={} billTime={} carNumber={} paymentStatus={}"
                ,billStaff,billDep,billTime,carNumber,paymentStatus);
        return resultObj;
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


    @GetMapping(value = "/uploadViolationImg")
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
        LOGGER.info("结束查询违章图片 img {}",violationImg.getOriginalFilename());
        return resultObj;
    }



}
