package com.xyc.userc.service;

import com.xyc.userc.vo.BlacklistVo;

import java.util.List;

/**
 * Created by 1 on 2020/8/24.
 */
public interface BlacklistService
{
    List<BlacklistVo> getBlacklist(String carNum, String createName, String createMobile, String page, String size) throws Exception;

    void addBlacklist(String mobile, String reason, Long createUserId) throws Exception;

    void removeBlacklist(String carNum) throws Exception;

    void refreshBlackListEnter() throws  Exception;
}
