package com.xyc.userc.dao;

import com.xyc.userc.entity.Requestpath;

import java.util.List;

/**
 * Created by 1 on 2020/7/20.
 */
public interface RequestpathMapper
{
    List<Requestpath> selectByPermissionId(Long permissionId);
}
