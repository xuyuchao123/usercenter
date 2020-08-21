package com.xyc.userc.dao;

import com.xyc.userc.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RoleMapper {

    List<Role> selectByOpenId(String openId);

    void insertUserRole(@Param("mobileOpenIdId")Integer mobileOpenIdId, @Param("roleId")Integer roleId,
                        @Param("gmtCreate")Date gmtCreate, @Param("gmtModified")Date gmtModified);

    int selectIdByRoleCode(@Param("roleCode")String roleCode);
}