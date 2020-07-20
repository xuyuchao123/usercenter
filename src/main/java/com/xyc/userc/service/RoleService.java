package com.xyc.userc.service;

import com.xyc.userc.entity.Role;

import java.util.List;

/**
 * Created by 1 on 2020/7/17.
 */
public interface RoleService
{
    List<Long> queryIdListByPath(String url) throws Exception;
}
