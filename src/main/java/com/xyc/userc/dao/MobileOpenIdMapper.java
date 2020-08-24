package com.xyc.userc.dao;

import com.xyc.userc.entity.MobileOpenId;
import org.apache.ibatis.annotations.Param;

/**
 * Created by 1 on 2020/8/18.
 */
public interface MobileOpenIdMapper
{
    int insertMobileOpenId(MobileOpenId mobileOpenId);

    MobileOpenId selectByMobile(String mobile);
}
