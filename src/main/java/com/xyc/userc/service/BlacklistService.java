package com.xyc.userc.service;

import com.xyc.userc.vo.BlacklistVo;

import java.util.List;

/**
 * Created by 1 on 2020/8/24.
 */
public interface BlacklistService
{
    List<BlacklistVo> getBlacklist(String name, String mobile, String createName, String createMobile) throws Exception;

    void addBlacklist(String mobile, String reason, String openId) throws Exception;

    void removeBlacklist(String mobile) throws Exception;
}
