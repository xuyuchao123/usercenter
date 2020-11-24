package com.xyc.userc.dao;


import com.xyc.userc.vo.UserInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserMapper
{
    List<Map> selectUserRoleByMobile(String mobile);

    List<Map> selectUserRoleByOpenIdRoleCode(@Param("openId")String openId, @Param("roleCode")String roleCode);

    List<Map> selectUserIdByMobile(List<String> mobiles);

    String selectUserId(String mobile);

    void updateRoleIdByMobileOpenId(@Param("roleId")int roleId, @Param("mobileOpenIdId")int mobileOpenIdId, @Param("gmtModified")Date gmtModified);

    void updateRoleIdByMobileOpenIdRoleId(@Param("roleId")int roleId, @Param("mobileOpenIdId")int mobileOpenIdId, @Param("oriRoleId")int oriRoleId,
                                          @Param("gmtModified")Date gmtModified);

    List<UserInfoVo> selectUserInfoVo(@Param("openId")String openId);

    Map selectStockCodeInfo(String openId);
}