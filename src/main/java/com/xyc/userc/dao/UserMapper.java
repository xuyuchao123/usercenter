package com.xyc.userc.dao;


import com.xyc.userc.vo.UserInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserMapper
{
    List<Map> selectUserRoleByOpenId(String mobile);

    List<Map> selectUserIdByMobile(List<String> mobiles);

    String selectUserId(String mobile);

    void updateRoleIdByMobileOpenId(@Param("roleId")int roleId, @Param("mobileOpenIdId")int mobileOpenIdId, @Param("gmtModified")Date gmtModified);

    List<UserInfoVo> selectUserInfoVo(@Param("openId")String openId);

    Map selectStockCodeInfo(String openId);
}