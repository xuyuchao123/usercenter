package com.xyc.userc.controller;

import com.xyc.userc.service.RoleService;
import com.xyc.userc.util.CommonExceptionHandler;
import com.xyc.userc.util.JsonResultObj;
import com.xyc.userc.util.JsonResultObj_Page;
import com.xyc.userc.vo.BindedUserRoleVo;
import com.xyc.userc.vo.BlacklistVo;
import com.xyc.userc.vo.DefaultUserRoleVo;
import com.xyc.userc.vo.RoleVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 1 on 2020/12/3.
 */
@RestController
@CrossOrigin
@RequestMapping("/role")
@Api(tags = "角色管理相关api")
public class RoleManageController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(RoleManageController.class);

    @Resource
    RoleService roleService;

    @PostMapping("/queryDefaultUserRole")
    @ApiOperation(value="查询用户预置角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="mobile", value="手机号", required=false, dataType="String"),
            @ApiImplicitParam(name="page", value="当前页码", required=true, dataType="String"),
            @ApiImplicitParam(name="size", value="每页记录条数", required=true, dataType="String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj_Page queryDefaultUserRole(String mobile, String page, String size)
    {
        LOGGER.info("开始查询用户预置角色信息 mobile={} page={} size={}" ,mobile,page,size);
        JsonResultObj_Page resultObj_page = null;
        try
        {
            List resList = roleService.getDefaultUserRole(mobile,page,size);
            List<DefaultUserRoleVo> defaultUserRoleVos = null;
            String total = null;
            if(resList != null && resList.size() == 4)
            {
                total = resList.get(0).toString();
                page = resList.get(1).toString();
                size = resList.get(2).toString();
                defaultUserRoleVos = (List<DefaultUserRoleVo>)resList.get(3);
            }
            resultObj_page = new JsonResultObj_Page(true,defaultUserRoleVos,total,page,size);
        }
        catch (Exception e)
        {
            resultObj_page = CommonExceptionHandler.handException_page(e, "查询用户默认角色配置信息失败", LOGGER, resultObj_page);
        }
        LOGGER.info("结束查询用户预置角色信息 mobile={} page={} size={}" ,mobile,page,size);
        return resultObj_page;
    }


    @PostMapping("/updateDefaultUserRole")
    @ApiOperation(value="新增/修改用户预置角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="jobNum", value="工号", required=true, dataType="String"),
            @ApiImplicitParam(name="mobile", value="手机号", required=true, dataType="String"),
            @ApiImplicitParam(name="role", value="角色id列表", required=true, dataType="String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：操作成功 isSuccess=false：操作失败，resMsg为错误信息")})
    public JsonResultObj updateDefaultUserRole(String jobNum, String mobile, String role)
    {
        LOGGER.info("开始新增/修改用户预置角色信息 jobNum={} mobile={} role={}",jobNum,mobile,role);
        JsonResultObj resultObj = null;
        try
        {
            roleService.addModifyDefaultUserRole(jobNum,mobile,role);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "新增/修改用户默认角色配置信息失败", LOGGER, resultObj);
        }
        LOGGER.info("结束新增/修改用户预置角色信息 jobNum={} mobile={} role={}",jobNum,mobile,role);
        return resultObj;
    }

    @PostMapping("/deleteDefaultUserRole")
    @ApiOperation(value="删除用户预置角色信息")
    @ApiImplicitParam(name="mobile", value="手机号", required=true, dataType="String")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：操作成功 isSuccess=false：操作失败，resMsg为错误信息")})
    public JsonResultObj deleteDefaultUserRole(String jobNum, String mobile, String role)
    {
        LOGGER.info("开始删除用户预置角色信息 jobNum={} mobile={} role={}",jobNum,mobile,role);
        JsonResultObj resultObj = null;
        try
        {
            roleService.removeDefaultUserRole(mobile);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "删除用户默认角色配置信息失败", LOGGER, resultObj);
        }
        LOGGER.info("结束删除用户预置角色信息 jobNum={} mobile={} role={}",jobNum,mobile,role);
        return resultObj;
    }


    @PostMapping("/queryAllRole")
    @ApiOperation(value="查询所有角色列表")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj queryAllRole()
    {
        LOGGER.info("开始查询所有角色列表");
        JsonResultObj resultObj = null;
        try
        {
            List<RoleVo> roleVos = roleService.getAllRole();
            resultObj = new JsonResultObj(true,roleVos);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "查询黑名单失败", LOGGER, resultObj);
        }
        LOGGER.info("结束查询所有角色列表");
        return resultObj;
    }

    @PostMapping("/queryBindedUserRole")
    @ApiOperation(value="查询用户已绑定角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="mobile", value="手机号", required=false, dataType="String"),
            @ApiImplicitParam(name="page", value="当前页码", required=true, dataType="String"),
            @ApiImplicitParam(name="size", value="每页记录条数", required=true, dataType="String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj_Page queryBindedUserRole(String mobile, String page, String size)
    {
        LOGGER.info("开始查询用户已绑定角色信息 mobile={} page={} size={}" ,mobile,page,size);
        JsonResultObj_Page resultObj_page = null;
        try
        {
            List resList = roleService.getBindedUserRole(mobile,page,size);
            List<BindedUserRoleVo> bindedUserRoleVos = null;
            String total = null;
            if(resList != null && resList.size() == 4)
            {
                total = resList.get(0).toString();
                page = resList.get(1).toString();
                size = resList.get(2).toString();
                bindedUserRoleVos = (List<BindedUserRoleVo>)resList.get(3);
            }
            resultObj_page = new JsonResultObj_Page(true,bindedUserRoleVos,total,page,size);
        }
        catch (Exception e)
        {
            resultObj_page = CommonExceptionHandler.handException_page(e, "查询用户已绑定角色信息失败", LOGGER, resultObj_page);
        }
        LOGGER.info("结束查询用户已绑定角色信息 mobile={} page={} size={}" ,mobile,page,size);
        return resultObj_page;
    }

    @PostMapping("/updateBindedUserRole")
    @ApiOperation(value="修改用户已绑定角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="mobile", value="手机号", required=true, dataType="String"),
            @ApiImplicitParam(name="role", value="角色id列表", required=true, dataType="String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：修改成功 isSuccess=false：修改失败，resMsg为错误信息")})
    public JsonResultObj updateBindedUserRole(String mobile, String role)
    {
        LOGGER.info("开始修改用户已绑定角色信息 mobile={} role={}" ,mobile,role);
        JsonResultObj resultObj = null;
        try
        {
            roleService.modifyBindedUserRole(mobile,role);
            resultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "修改用户已绑定角色信息失败", LOGGER, resultObj);
        }
        LOGGER.info("结束修改用户已绑定角色信息 mobile={} role={}" ,mobile,role);
        return resultObj;
    }


}
