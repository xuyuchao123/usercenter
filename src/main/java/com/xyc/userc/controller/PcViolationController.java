package com.xyc.userc.controller;

import com.xyc.userc.entity.ViolationInfo;
import com.xyc.userc.service.ViolationService;
import com.xyc.userc.util.CommonExceptionHandler;
import com.xyc.userc.util.JsonResultObj;
import com.xyc.userc.util.JsonResultObj_Page;
import com.xyc.userc.vo.*;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
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
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj addViolationInfo(@Validated ViolationInfoAddVo vo)
    {
        LOGGER.info("开始新增违章信息 {}",vo.toString());
        JsonResultObj resultObj = null;
        try
        {
            violationService.addViolationInfo(vo.getBillingDepartment(),vo.getBillingTime(),vo.getOpenSingle(),vo.getBillingSerialNumber(),
                    vo.getTheAmountOfTheFine(),vo.getIllegalPictures(),vo.getPaymentStatus(),vo.getReasonForFine());
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException_page(e, "新增违章信息失败", LOGGER);
        }
        LOGGER.info("结束新增违章信息 {}",vo.toString());
        return resultObj;
    }

    @PostMapping("/form/query")
    @ApiOperation(value="查询违章信息")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj_Page<ViolationInfoVo> queryViolationInfo(@Validated ViolationInfoQueryVo vo)
    {
        LOGGER.info("开始查询违章信息 {}",vo.toString());
        JsonResultObj_Page resultObj_page = null;
        try
        {
            List resList = violationService.getViolationInfo(vo.getBillingMethod(),vo.getBillingDepartment(),vo.getBillingTime(),
                    vo.getPaymentStatus(),vo.getBillingSerialNumber(),vo.getPage(),vo.getSize());
            List<ViolationInfoVo> violationInfoVos = null;
            String total = null;
            String page = null;
            String size = null;
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

        LOGGER.info("结束查询违章信息 {}",vo.toString());
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
    @ApiImplicitParam(name="billingSerialNumber", value="开单序号", required=true, allowMultiple=true, dataType = "String")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：删除成功 isSuccess=false：删除失败，resMsg为错误信息")})
    public JsonResultObj removeViolationInfo(@NotNull(message = "开单序号不能为空") @RequestParam("billingSerialNumber")String[] billNums)
    {
        List<String> billNumList = new ArrayList<>();
        for(int i = 0; i < billNums.length; i++)
        {
            billNumList.add(billNums[i]);
        }
        LOGGER.info("开始删除违章信息 billNum={}",billNumList.toString());

        JsonResultObj resultObj = null;
        try
        {
            violationService.removeViolationInfo(billNumList);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException_page(e, "删除违章信息失败", LOGGER);
        }

        LOGGER.info("结束删除违章信息 billNum={}",billNumList.toString());
        return resultObj;
    }


}
