package com.xyc.userc.service.impl;

import com.avei.shriety.wx_sdk.pojo.Userinfo;
import com.xyc.userc.dao.MobileMapper;
import com.xyc.userc.dao.RoleMapper;
import com.xyc.userc.dao.UserMapper;
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

import java.util.Date;
import java.util.List;

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
    private BCryptPasswordEncoder bCryptPasswordEncoder;

//    重写认证方法,实现自定义springsecurity用户认证（用户名密码登录）
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException
    {
        LOGGER.info("进入通过用户名获取用户信息权限信息方法");
        User user = userMapper.selectByUsername(userName);
        if (user == null)
        {
            throw new UsernameNotFoundException("用户名不存在!");
        }

        LOGGER.info("结束通过用户名获取用户信息权限信息方法");
        return user;
    }

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
    public void addUser(String userName, String password, String mobile, String userRealName, Byte isDelete,
                        Byte isEnable, Byte isLocked, Long userCreate, String mesCode) throws Exception
    {
        LOGGER.info("进入新增用户方法 mobile={} mesCode={}",mobile,mesCode);
        String storedMesCode = mobileMapper.selectMesCodeByMobile(mobile);
        if(storedMesCode == null)
        {
            LOGGER.info("短信验证码过期 mobile={}",mobile);
            throw new MesCodeExpiredException("短信验证码过期，请点击重新发送");
        }
        if(!storedMesCode.equals(mesCode))
        {
            LOGGER.info("短信验证码错误 mobile={} storedMesCode={} mesCode={}",mobile,storedMesCode,mesCode);
            throw new MesCodeErrorException("短信验证码错误");
        }
        else        //验证通过，将当前验证码改为无效状态
        {
            byte status = 0;
            LOGGER.info("验证通过，将当前验证码改为无效状态 mobile={} storedMesCode={}",mobile,storedMesCode);
            mobileMapper.updateMesCodeStatus(mobile,status,new Date());
        }
        User user = new User();
        user.setIsEnable(isEnable);
        int id = userMapper.insert(user);
        System.out.println(id);
        LOGGER.info("结束新增用户方法 mobile={} mesCode={}",mobile,mesCode);
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
}
