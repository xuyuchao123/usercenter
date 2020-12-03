package com.xyc.userc.dao;

import com.xyc.userc.entity.Role;
import com.xyc.userc.vo.DefaultRoleVo;
import com.xyc.userc.vo.MobileOpenIdRoleVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RoleMapper {

    List<Role> selectByOpenId(String openId);

    void insertUserRole(@Param("mobileOpenIdId")Integer mobileOpenIdId, @Param("roleId")Integer roleId,
                        @Param("gmtCreate")Date gmtCreate, @Param("gmtModified")Date gmtModified);

    Role selectByRoleCode(@Param("roleCode")String roleCode);

    List<MobileOpenIdRoleVo> selectMobileOpenIdRoleVo(String openId);

    void updateUserRole(@Param("mobileOpenIdId")Integer mobileOpenIdId, @Param("roleId")Integer roleId,
                        @Param("gmtModified")Date gmtModified);

    void deleteUserRole(@Param("mobileOpenIdId")Integer mobileOpenIdId);

    List<DefaultRoleVo> selectDefaultRole(@Param("jobNum")String jobNum, @Param("mobile")String mobile, @Param("roleName")String roleName);

}