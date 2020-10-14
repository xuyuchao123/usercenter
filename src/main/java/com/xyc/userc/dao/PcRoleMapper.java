package com.xyc.userc.dao;

import com.xyc.userc.entity.PcRole;
import com.xyc.userc.entity.Role;

import java.util.List;

/**
 * Created by 1 on 2020/10/14.
 */
public interface PcRoleMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(PcRole record);

    PcRole selectByPrimaryKey(Long id);

    List<PcRole> selectAll();

    int updateByPrimaryKey(PcRole record);

    List<PcRole> selectByUserId(Long userId);

    List<Long> selectIdsByPath(String url);
}
