package com.xyc.userc.dao;

import com.xyc.userc.entity.Role;
import java.util.List;

public interface RoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    Role selectByPrimaryKey(Long id);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);

    List<Role> selectByUserId(Long userId);

    List<Long> selectIdsByPath(String url);
}