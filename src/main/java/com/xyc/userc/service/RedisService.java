package com.xyc.userc.service;

/**
 * Created by 1 on 2020/12/8.
 */
public interface RedisService
{
    void storeUserInfoVo() throws Exception;

    void updateRedis(String openId) throws Exception;

}
