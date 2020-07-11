package com.xyc.userc.service;

import com.xyc.userc.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * Created by 1 on 2020/6/15.
 */
public interface UserService extends UserDetailsService
{
     void addUser(String userName, String password, String userRealName, Byte isDelete,
                        Byte isEnable, Byte isLocked, Long userCreate) throws Exception;
     User loadUserByMobile(String var1) throws UsernameNotFoundException;

     int checkUserExistByMobile(String mobile) throws Exception;

     void updatePassword(String mobile, String newPassword) throws Exception;

     int checkUserRegByUsername(String username) throws Exception;

     int checkUserRegByMobile(String mobile) throws Exception;
}
