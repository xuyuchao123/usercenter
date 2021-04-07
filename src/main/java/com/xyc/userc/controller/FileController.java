package com.xyc.userc.controller;

import com.xyc.userc.entity.Student;
import com.xyc.userc.service.BlacklistService;
import com.xyc.userc.service.FileService;
import com.xyc.userc.util.CommonExceptionHandler;
import com.xyc.userc.util.JsonResultObj;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

/**
 * Created by 1 on 2020/10/16.
 */
@RestController
@CrossOrigin
@RequestMapping("/mes")
//@Api(tags = "附件管理相关api")
@ApiIgnore
public class FileController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @Resource
    FileService fileService;

    @PostMapping("/uploadAttachment")
//    @ApiOperation(value = "上传附件")
//    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "要上传的附件", required = true),
//            @ApiImplicitParam(name = "", value = "", required = true, dataType = "String")})
//    @ApiResponses({@ApiResponse(code = 200, message = "isSuccess=true：绑定成功 isSuccess=false：绑定失败，resMsg为错误信息")})
    public JsonResultObj bindMobile(@ApiIgnore HttpServletRequest request,@RequestBody Student[] students)
    {
//        LOGGER.info("开始上传附件 filename:{}", file.getOriginalFilename());
        JsonResultObj resultObj = null;
//        try
//        {
//            fileService.uploadAttach(file);
//            resultObj = new JsonResultObj(true);
//        }
//        catch (Exception e)
//        {
//            resultObj = CommonExceptionHandler.handException(e, "上传附件失败", LOGGER);
//        }
//        LOGGER.info("结束上传附件 filename:{}", file.getOriginalFilename());
        return resultObj;
    }
}
