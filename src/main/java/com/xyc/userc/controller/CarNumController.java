package com.xyc.userc.controller;

import com.avei.shriety.wx_sdk.constant.WxsdkConstant;
import com.xyc.userc.entity.CarNumOpenId;
import com.xyc.userc.entity.User;
import com.xyc.userc.service.CarNumService;
import com.xyc.userc.util.CommonExceptionHandler;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.JsonResultObj;
import com.xyc.userc.vo.CarNumInOutTimeVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
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
    @ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号", required = false, dataType = "String"),
            @ApiImplicitParam(name = "carNum", value = "车牌号", required = false, dataType = "String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj queryCarNum(String mobile, String carNum)
    {
        LOGGER.info("开始查询车牌号mobile={},carNum={}",mobile,carNum);
        JsonResultObj resultObj = null;
        try
        {
            List<CarNumOpenId> carNumOpenIdList = carNumService.getCarNum(mobile,carNum);
            resultObj = new JsonResultObj(true,carNumOpenIdList);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "查询车牌号失败", LOGGER, resultObj);
        }
        LOGGER.info("结束查询车牌号");
        return resultObj;
    }

    @PostMapping("/deleteCarNum")
    @ApiOperation(value="删除车牌号")
    @ApiImplicitParam(name = "carNum", value = "车牌号", required = true, dataType = "String")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：删除成功 isSuccess=false：删除失败，resMsg为错误信息")})
    public JsonResultObj deleteCarNum(String carNum, @ApiIgnore HttpSession session)
    {
        LOGGER.info("开始删除车牌号 carNum={}",carNum);
        JsonResultObj resultObj = null;
        User user = (User) session.getAttribute(WxsdkConstant.USERINFO);
        if(user == null)
        {
            user = new User("oPh4uszJ0L7a9zNRU-tw4smPtbfU","一人！一车！一世界！","1","山东","临沂","中国",
                    "http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJPHH4qzibNINxpqxUnZEeibiagxgibibiaB" +
                            "2EM9DXt7CLNpgmjewP5lsIoR0HQ1Cqzq46K1Dz93jdAQj4g/132",null,null,"13167068999",null);
        }
        if(user == null)
        {
            LOGGER.info("session中不存在用户信息");
            resultObj = new JsonResultObj(false, JsonResultEnum.USER_INFO_NOT_EXIST);
            return resultObj;
        }
        String openId = user.getOpenid();
        try
        {
            carNumService.removeCarNum(carNum,openId);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "删除车牌号失败", LOGGER, resultObj);
        }
        LOGGER.info("结束删除车牌号");
        return resultObj;
    }

    @PostMapping("/addCarNum")
    @ApiOperation(value="新增车牌号")
    @ApiImplicitParam(name = "carNum", value = "车牌号", required = true, dataType = "String")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：新增成功 isSuccess=false：新增失败，resMsg为错误信息")})
    public JsonResultObj addCarNum(String carNum, @ApiIgnore HttpSession session)
    {
        LOGGER.info("开始新增车牌号 carNum={}", carNum);
        JsonResultObj resultObj = null;
        User user = (User) session.getAttribute(WxsdkConstant.USERINFO);
        if(user == null)
        {
            user = new User("oPh4uszJ0L7a9zNRU-tw4smPtbfU","一人！一车！一世界！","1","山东","临沂","中国",
                    "http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJPHH4qzibNINxpqxUnZEeibiagxgibibiaB" +
                            "2EM9DXt7CLNpgmjewP5lsIoR0HQ1Cqzq46K1Dz93jdAQj4g/132",null,null,"13167068999",null);
        }
        if(user == null)
        {
            LOGGER.info("session中不存在用户信息");
            resultObj = new JsonResultObj(false, JsonResultEnum.USER_INFO_NOT_EXIST);
            return resultObj;
        }
        String openId = user.getOpenid();
        try
        {
            carNumService.addCarNum(carNum,openId);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "新增车牌号失败", LOGGER, resultObj);
        }
        LOGGER.info("结束新增车牌号 carNum={}", carNum);
        return resultObj;
    }

    @PostMapping("/updateCarNum")
    @ApiOperation(value="修改车牌号")
    @ApiImplicitParams({@ApiImplicitParam(name="oldCarNum", value="原来的车牌号", required=true, dataType="String"),
            @ApiImplicitParam(name="newCarNum", value="新的车牌号", required=true, dataType="String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：修改成功 isSuccess=false：修改失败，resMsg为错误信息")})
    public JsonResultObj updateCarNum(String oldCarNum, String newCarNum, @ApiIgnore HttpSession session) {
        LOGGER.info("开始修改车牌号 oldCarNum={} newCarNum={}", oldCarNum, newCarNum);
        JsonResultObj resultObj = null;
        User user = (User) session.getAttribute(WxsdkConstant.USERINFO);
        if(user == null)
        {
            user = new User("oPh4uszJ0L7a9zNRU-tw4smPtbfU","一人！一车！一世界！","1","山东","临沂","中国",
                    "http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJPHH4qzibNINxpqxUnZEeibiagxgibibiaB" +
                            "2EM9DXt7CLNpgmjewP5lsIoR0HQ1Cqzq46K1Dz93jdAQj4g/132",null,null,"13167068999",null);
        }
        if (user == null) {
            LOGGER.info("session中不存在用户信息");
            resultObj = new JsonResultObj(false, JsonResultEnum.USER_INFO_NOT_EXIST);
            return resultObj;
        }
        String openId = user.getOpenid();

        try
        {
            carNumService.modifyCarNumByOpenId(oldCarNum, newCarNum, openId);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "修改车牌号失败", LOGGER, resultObj);
        }
        LOGGER.info("结束修改车牌号 oldCarNum={} newCarNum={}", oldCarNum, newCarNum);
        return resultObj;
    }

    @PostMapping("/enableCarNum")
    @ApiOperation(value="启用车牌号")
    @ApiImplicitParam(name = "carNum", value = "车牌号", required = false, dataType = "String")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：启用成功 isSuccess=false：启用失败，resMsg为错误信息")})
    public JsonResultObj enableCarNum(String carNum, @ApiIgnore HttpSession session)
    {
        LOGGER.info("开始启用车牌号 carNum={}",carNum);
        JsonResultObj resultObj = null;
        User user = (User) session.getAttribute(WxsdkConstant.USERINFO);
        if(user == null)
        {
            user = new User("oPh4uszJ0L7a9zNRU-tw4smPtbfU","一人！一车！一世界！","1","山东","临沂","中国",
                    "http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJPHH4qzibNINxpqxUnZEeibiagxgibibiaB" +
                            "2EM9DXt7CLNpgmjewP5lsIoR0HQ1Cqzq46K1Dz93jdAQj4g/132",null,null,"13167068999",null);
        }
        if (user == null) {
            LOGGER.info("session中不存在用户信息");
            resultObj = new JsonResultObj(false, JsonResultEnum.USER_INFO_NOT_EXIST);
            return resultObj;
        }
        String openId = user.getOpenid();
        try
        {
            carNumService.enableCarNum(carNum, openId);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "启用车牌号失败", LOGGER, resultObj);
        }
        LOGGER.info("结束启用车牌号");
        return resultObj;
    }


    @PostMapping("/queryInOutTime")
    @ApiOperation(value="查询车辆进出厂时间")
    @ApiImplicitParam(name = "carNum", value = "车牌号", required = false, dataType = "String")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj enableCarNum(String carNum)
    {
        LOGGER.info("开始查询车辆进出厂时间 carNum={}", carNum);
        JsonResultObj resultObj = null;
        try
        {
            List<CarNumInOutTimeVo> carNumInOutTimeVos = carNumService.queryInOutTime(carNum);
            resultObj = new JsonResultObj(true,carNumInOutTimeVos);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "查询车辆进出厂时间失败", LOGGER, resultObj);
        }
        LOGGER.info("结束查询车辆进出厂时间");
        return resultObj;
    }
}
