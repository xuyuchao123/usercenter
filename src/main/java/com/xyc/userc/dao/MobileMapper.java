package com.xyc.userc.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

/**
 * Created by 1 on 2020/7/8.
 */
public interface MobileMapper
{
    public String selectMesCodeByMobile(String mobile);

    public void insertMesCode(@Param("mobile")String mobile, @Param("mesCode")String mesCode,
                              @Param("status")int status, @Param("gmtCreate")Date gmtCreate,
                              @Param("gmtModified")Date gmtModified);

    public void updateMesCodeStatus(@Param("mobile")String mobile, @Param("status")int status,
                                    @Param("gmtModified")Date gmtModified);

    public String selectValidMesCode(@Param("mobile")String mobile);

}
