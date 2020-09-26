package com.xyc.userc.service;

import com.avei.shriety.wx_sdk.pojo.Userinfo;
import com.xyc.userc.entity.CarNumOpenId;
import com.xyc.userc.entity.Role;
import com.xyc.userc.entity.User;
import com.xyc.userc.vo.UserInfoVo;


import java.util.List;

/**
 * Created by 1 on 2020/6/15.
 */
public interface UserService
{
     User getUser(User user) throws Exception;

     Role bindMobileToOpenId(String mobile, String mesCode, String openId) throws Exception;

     UserInfoVo getUserInfoVo() throws Exception;

}
