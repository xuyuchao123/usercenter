package com.xyc.userc.service;


import java.util.List;

/**
 * Created by 1 on 2020/7/17.
 */
public interface PcRoleService
{
    List<Long> queryIdListByPath(String url) throws Exception;
}
