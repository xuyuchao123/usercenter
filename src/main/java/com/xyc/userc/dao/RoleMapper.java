package com.xyc.userc.dao;

import com.xyc.userc.entity.Role;
import com.xyc.userc.vo.DefaultUserRoleVo;
import com.xyc.userc.vo.MobileOpenIdRoleVo;
import com.xyc.userc.vo.RoleVo;
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

    List<DefaultUserRoleVo> selectDefaultUserRole(@Param("mobile")String mobile);

    List<RoleVo> selectAllRole();

}