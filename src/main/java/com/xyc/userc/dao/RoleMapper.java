package com.xyc.userc.dao;

import com.xyc.userc.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    Role selectByPrimaryKey(Long id);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);

    List<Role> selectByUserId(Long userId);

    List<Long> selectIdsByPath(String url);

    List<Role> selectByOpenId(String openId);

    void insertUserRole(@Param("mobileOpenIdId")Integer mobileOpenIdId, @Param("roleId")Integer roleId,
                        @Param("gmtCreate")Date gmtCreate, @Param("gmtModified")Date gmtModified);
}