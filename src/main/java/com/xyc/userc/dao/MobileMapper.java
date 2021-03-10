package com.xyc.userc.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 2020/7/8.
 */
public interface MobileMapper
{
     String selectMesCodeByMobile(String mobile);

     void insertMesCode(@Param("mobile")String mobile, @Param("mesCode")String mesCode,
                              @Param("status")int status, @Param("gmtCreate")Date gmtCreate,
                              @Param("gmtModified")Date gmtModified);

     void updateMesCodeStatus(@Param("mobile")String mobile, @Param("status")int status,
                                    @Param("gmtModified")Date gmtModified);

     String selectValidMesCode(@Param("mobile")String mobile);



     String selectPcMesCodeByMobile(String mobile);

     void insertPcMesCode(@Param("mobile")String mobile, @Param("mesCode")String mesCode,
                              @Param("status")Byte status, @Param("gmtCreate")Date gmtCreate,
                              @Param("gmtModified")Date gmtModified);

     void updatePcMesCodeStatus(@Param("mobile")String mobile, @Param("status")Byte status,
                                    @Param("gmtModified")Date gmtModified);

     String selectValidPcMesCode(@Param("mobile")String mobile);

     List<String> selectMobileByCarNum(@Param("carNum")String carNum);




}
