package com.xyc.userc.service;

import com.avei.shriety.wx_sdk.pojo.Userinfo;
import com.xyc.userc.entity.CarNumOpenId;
import com.xyc.userc.entity.Role;
import com.xyc.userc.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * Created by 1 on 2020/6/15.
 */
public interface UserService
{
     User loadUserByMobile(String var1) throws UsernameNotFoundException;

     int checkUserExistByMobile(String mobile) throws Exception;

     void updatePassword(String mobile, String newPassword) throws Exception;

     int checkUserRegByUsername(String username) throws Exception;

     int checkUserRegByMobile(String mobile) throws Exception;

     User getUser(User user) throws Exception;

     Role bindMobileToOpenId(String mobile, String mesCode, String openId) throws Exception;


}
