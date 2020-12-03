package com.xyc.userc.controller;

import com.xyc.userc.service.RoleService;
import com.xyc.userc.util.CommonExceptionHandler;
import com.xyc.userc.util.JsonResultObj;
import com.xyc.userc.util.JsonResultObj_Page;
import com.xyc.userc.vo.BlacklistVo;
import com.xyc.userc.vo.DefaultRoleVo;
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

    @PostMapping("/queryDefaultRole")
    @ApiOperation(value="查询用户默认角色配置信息")
    @ApiImplicitParams({@ApiImplicitParam(name="jobNum", value="工号", required=false, dataType="String"),
            @ApiImplicitParam(name="mobile", value="手机号", required=false, dataType="String"),
            @ApiImplicitParam(name="roleName", value="角色名称", required=false, dataType="String")})
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功 isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj queryBlacklist(String jobNum, String mobile, String roleName)
    {
        LOGGER.info("开始查询用户默认角色配置信息 jobNum={} mobile={} roleName={}",jobNum,mobile,roleName);
        JsonResultObj resultObj = null;
        try
        {
            List<DefaultRoleVo> defaultRoleVos = roleService.getDefaultRole(jobNum,mobile,roleName);
            String total = null;
            resultObj = new JsonResultObj(true,defaultRoleVos);
        }
        catch (Exception e)
        {
            resultObj = CommonExceptionHandler.handException(e, "查询黑名单失败", LOGGER, resultObj);
        }
        LOGGER.info("结束查询用户默认角色配置信息 jobNum={} mobile={} roleName={}",jobNum,mobile,roleName);
        return resultObj;
    }


}
