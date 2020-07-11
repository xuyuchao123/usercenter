package com.xyc.userc.dao;

import com.xyc.userc.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    User selectByPrimaryKey(Long id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    User selectByUsername(@Param("userName")String userName);

    User selectByMobile(@Param("mobile")String mobile);

    Integer checkUserByMobile(@Param("mobile")String mobile,@Param("isDeleted")Byte isDeleted
                            ,@Param("isEnable")Byte isEnable,@Param("isLocked")Byte isLocked );

    Integer selectRegUserByUsername(@Param("username")String username, @Param("isDeleted")Byte isDeleted);

    Integer selectRegUserByMobile(@Param("mobile")String mobile, @Param("isDeleted")Byte isDeleted);

    void updatePwdByMobile(@Param("mobile")String mobile, @Param("newPassword")String newPassword);
}