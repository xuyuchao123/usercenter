package com.xyc.userc.dao;

import com.xyc.userc.entity.MobileOpenId;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 2020/8/18.
 */
public interface MobileOpenIdMapper
{
    int insertMobileOpenId(MobileOpenId mobileOpenId);

    MobileOpenId selectByMobileOpenId(@Param("mobile")String mobile, @Param("openId")String openId);

    List<Map> selectByMobileOpenIdRole(@Param("mobile")String mobile, @Param("openId")String openId);

    void updateMobile(@Param("mobile")String mobile, @Param("openId")String openId);
}
