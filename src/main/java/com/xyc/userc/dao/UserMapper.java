package com.xyc.userc.dao;

import com.xyc.userc.entity.Role;
import com.xyc.userc.entity.User;
import com.xyc.userc.vo.UserInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserMapper
{
    List<Map> selectUserRoleByOpenId(String openId);

    void updateRoleIdByMobileOpenId(@Param("roleId")int roleId, @Param("mobileOpenIdId")int mobileOpenIdId, @Param("gmtModified")Date gmtModified);

    List<UserInfoVo> selectUserInfoVo();
}