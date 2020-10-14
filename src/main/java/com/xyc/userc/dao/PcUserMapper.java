package com.xyc.userc.dao;

import com.xyc.userc.entity.PcUser;
import com.xyc.userc.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 1 on 2020/10/14.
 */
public interface PcUserMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(PcUser record);

    PcUser selectByPrimaryKey(Long id);

    List<PcUser> selectAll();

    int updateByPrimaryKey(PcUser record);

    PcUser selectByUsername(@Param("userName")String userName);

    PcUser selectByMobile(@Param("mobile")String mobile);

    Integer checkUserByMobile(@Param("mobile")String mobile,@Param("isDeleted")Byte isDeleted
            ,@Param("isEnable")Byte isEnable,@Param("isLocked")Byte isLocked );

    Integer selectRegUserByUsername(@Param("username")String username, @Param("isDeleted")Byte isDeleted);

    Integer selectRegUserByMobile(@Param("mobile")String mobile, @Param("isDeleted")Byte isDeleted);

    void updatePwdByMobile(@Param("mobile")String mobile, @Param("newPassword")String newPassword);
}
