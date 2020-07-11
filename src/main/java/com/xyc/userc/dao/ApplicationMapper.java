package com.xyc.userc.dao;

import com.xyc.userc.entity.Application;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApplicationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Application record);

    System selectByPrimaryKey(Long id);

    List<Application> selectAll();

    int updateByPrimaryKey(Application record);

    List<Application> selectApplication(@Param("appId")String appId, @Param("appName")String appName,
                                        @Param("createUserNum")String createUserNum, @Param("createUsername")String createUsername,
                                        @Param("isDeleted")Byte isDeleted);
}