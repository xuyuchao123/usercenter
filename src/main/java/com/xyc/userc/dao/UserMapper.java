package com.xyc.userc.dao;

import com.xyc.userc.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper
{
    Map selectUserRoleByOpenId(String openId);

    void updateRoleIdByMobileOpenId(@Param("roleId")int roleId, @Param("mobileOpenIdId")int mobileOpenIdId);

}