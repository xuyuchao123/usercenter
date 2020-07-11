package com.xyc.userc.service.impl;

import com.xyc.userc.dao.MobileMapper;
import com.xyc.userc.dao.UserMapper;
import com.xyc.userc.service.MobileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by 1 on 2020/7/8.
 */
@Service("mobileService")
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class MobileServiceImpl implements MobileService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(MobileServiceImpl.class);

    @Autowired
    private MobileMapper mobileMapper;

    //用手机号查询当前有效的短信验证码
    @Override
    public String loadMesCodeByMobile(String mobile) throws Exception
    {
        LOGGER.debug("进入通过手机号获取有效短信验证码方法 mobile={}",mobile);
        String mesCode = mobileMapper.selectMesCodeByMobile(mobile);
        LOGGER.debug("结束通过手机号获取有效短信验证码方法 mesCode={}",mesCode);
        return mesCode;
    }

    //手机号生成短信验证码后将其存入数据库
    @Override
    public String addMesCode(String mobile, String mesCode) throws Exception
    {
        LOGGER.debug("进入新增手机验证码方法 mobile={}, mesCode={}",mobile,mesCode);
        byte status = 1;
        Date gmtCreate = new Date();
        Date gmtModified = gmtCreate;
        mobileMapper.insertMesCode(mobile,mesCode,status,gmtCreate,gmtModified);
        LOGGER.debug("结束新增手机验证码方法");
        return null;
    }


    //短信验证码验证成功后将该验证码改为失效状态
    @Override
    public void setMesCodeInvalid(String mobile) throws Exception
    {
        LOGGER.debug("进入设置验证码失效状态方法 mobile={}", mobile);
        byte status = 0;
        Date gmtModified = new Date();
        mobileMapper.updateMesCodeStatus(mobile,status,gmtModified);
        LOGGER.debug("结束设置验证码失效状态方法 mobile={}", mobile);

    }

    //检查给定的手机号是否已经存在有效的短信验证码
//    @Override
//    public String getValidMesCode(String mobile) throws Exception
//    {
//        LOGGER.debug("开始查询有效短信验证码失方法 mobile={}", mobile);
//        mobileMapper.selectValidMesCode(mobile);
//        LOGGER.debug("结束查询有效短信验证码失方法 mobile={}", mobile);
//        return null;
//    }
}
