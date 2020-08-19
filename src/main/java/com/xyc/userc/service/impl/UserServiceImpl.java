package com.xyc.userc.service.impl;

import com.avei.shriety.wx_sdk.pojo.Userinfo;
import com.xyc.userc.dao.MobileMapper;
import com.xyc.userc.dao.MobileOpenIdMapper;
import com.xyc.userc.dao.RoleMapper;
import com.xyc.userc.dao.UserMapper;
import com.xyc.userc.entity.MobileOpenId;
import com.xyc.userc.entity.Role;
import com.xyc.userc.entity.User;
import com.xyc.userc.security.MesCodeErrorException;
import com.xyc.userc.security.MesCodeExpiredException;
import com.xyc.userc.security.MobileNotFoundException;
import com.xyc.userc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 2020/6/15.
 */
@Service("userService")
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class UserServiceImpl implements UserService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MobileMapper mobileMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MobileOpenIdMapper mobileOpenIdMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

//    重写认证方法,实现自定义springsecurity用户认证（用户名密码登录）
//    @Override
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException
//    {
//        LOGGER.info("进入通过用户名获取用户信息权限信息方法");
//        User user = userMapper.selectByUsername(userName);
//        if (user == null)
//        {
//            throw new UsernameNotFoundException("用户名不存在!");
//        }
//
//        LOGGER.info("结束通过用户名获取用户信息权限信息方法");
//        return user;
//    }

    @Override
    public User loadUserByMobile(String mobile) throws UsernameNotFoundException
    {
        LOGGER.info("进入通过手机号获取用户信息权限信息方法");
        User user = userMapper.selectByMobile(mobile);
        if (user == null)
        {
            throw new MobileNotFoundException("手机号未注册!");
        }

        LOGGER.info("结束通过手机号获取用户信息权限信息方法");
        return user;
    }

    @Override
    public int checkUserExistByMobile(String mobile) throws Exception
    {
        LOGGER.info("开始通过手机号查询用户信息方法");
        Integer mobileExist;
        byte isDeleted = 0;
        byte isEnable = 1;
        byte isLocked = 0;
        mobileExist = userMapper.checkUserByMobile(mobile,isDeleted,isEnable,isLocked);
        if(mobileExist == null)
        {
            mobileExist = 0;
        }
        LOGGER.info("结束通过手机号查询用户信息方法");
        return mobileExist;
    }

    @Override
    public void updatePassword(String mobile, String newPassword) throws Exception
    {
        LOGGER.info("进入更改用户密码方法mobile={} newPassword={}",mobile,newPassword);
        userMapper.updatePwdByMobile(mobile,bCryptPasswordEncoder.encode(newPassword));
        LOGGER.info("结束更改用户密码方法mobile={} newPassword={}",mobile,newPassword);

    }

    @Override
    public int checkUserRegByUsername(String username) throws Exception
    {
        LOGGER.info("开始通过用户名查询用户信息方法 username={}",username);
        Integer usernameExist;
        byte isDeleted = 0;
        usernameExist = userMapper.selectRegUserByUsername(username,isDeleted);
        if(usernameExist == null)
        {
            usernameExist = 0;
        }
        LOGGER.info("结束通过用户名查询用户信息方法 username={}",username);
        return usernameExist;
    }

    @Override
    public int checkUserRegByMobile(String mobile) throws Exception
    {
        LOGGER.info("开始通过手机号查询用户信息方法 mobile={}",mobile);
        Integer mobileExist;
        byte isDeleted = 0;
        mobileExist = userMapper.selectRegUserByMobile(mobile,isDeleted);
        if(mobileExist == null)
        {
            mobileExist = 0;
        }
        LOGGER.info("结束通过手机号查询用户信息方法 mobile={}",mobile);
        return mobileExist;
    }


    @Override
    public User getUser(User user) throws Exception
    {
        String openId = user.getOpenid();
        LOGGER.info("进入获取用户信息方法 openid：{}",openId);
        List<Role> roleList = roleMapper.selectByOpenId(openId);
        user.setRoles(roleList);
        return user;
    }

    @Override
    public Role bindMobileToOpenId(String mobile, String openId) throws Exception
    {
        LOGGER.info("进入绑定手机号方法 mobile:{} openid：{}",mobile,openId);
//        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        MobileOpenId mobileOpenId = new MobileOpenId(mobile,openId,openId,new Date());
        int i = mobileOpenIdMapper.insertMobileOpenId(mobileOpenId);
        Map map = userMapper.selectUserRoleByOpenId(openId);
        Role role = null;
        if(map != null)
        {
            int mobileOpenIdId = Integer.parseInt(map.get("MOBILEOPENIDID").toString());
            int roleId = Integer.parseInt(map.get("ROLEID").toString());
            Date date = new Date();
            roleMapper.insertUserRole(mobileOpenIdId,roleId,date,date);
            String roleName = (String)map.get("ROLENAME");
            String roleCode = (String)map.get("ROLECODE");
            int isDeleted = Integer.parseInt(map.get("ISDELETED").toString());
            Date gmtCreate = (Date)map.get("GMTCREATE");
            Date gmtModified = (Date)map.get("GMTMODIFIED");
            int parentRoleId = Integer.parseInt(map.get("PARENTROLEID").toString());
            role = new Role(roleId,roleName,roleCode,isDeleted,gmtCreate,gmtModified,parentRoleId);
        }
        LOGGER.info("结束绑定手机号方法 role：{}",role);
        return role;
    }
}
