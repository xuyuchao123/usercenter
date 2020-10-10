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
import smsservice.ISMS;
import smsservice.SMSService;

import java.util.Date;
import java.util.Random;

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
        LOGGER.info("进入通过手机号获取有效短信验证码方法 mobile={}",mobile);
        String mesCode = mobileMapper.selectMesCodeByMobile(mobile);
        LOGGER.info("结束通过手机号获取有效短信验证码方法 mesCode={}",mesCode);
        return mesCode;
    }

    //手机号生成短信验证码后将其存入数据库
    @Override
    public String addMesCode(String mobile, String mesCode) throws Exception
    {
        LOGGER.info("进入新增手机验证码方法 mobile={}, mesCode={}",mobile,mesCode);
        byte status = 1;
        Date gmtCreate = new Date();
        Date gmtModified = gmtCreate;
        mobileMapper.insertMesCode(mobile,mesCode,status,gmtCreate,gmtModified);
        LOGGER.info("结束新增手机验证码方法");
        return null;
    }


    //短信验证码验证成功后将该验证码改为失效状态
    @Override
    public void setMesCodeInvalid(String mobile) throws Exception
    {
        LOGGER.info("进入设置验证码失效状态方法 mobile={}", mobile);
        byte status = 0;
        Date gmtModified = new Date();
        mobileMapper.updateMesCodeStatus(mobile,status,gmtModified);
        LOGGER.info("结束设置验证码失效状态方法 mobile={}", mobile);

    }

    @Override
    public void checkAndSendMesCode(String mobile) throws Exception
    {
        LOGGER.info("进入检查并发送短信验证码方法 mobile={}",mobile);
        String mesCode = null;
        //检查是否存在有效的短信验证码
        mesCode = mobileMapper.selectMesCodeByMobile(mobile);
        //不存在有效验证码就随机生成一个6位数短信验证码
        if(mesCode == null)
        {
            mesCode =  String.valueOf(new Random().nextInt(899999) + 100000);
            //将新生成的验证码存入数据库
            int status = 1;
            Date gmtCreate = new Date();
            Date gmtModified = gmtCreate;
            mobileMapper.insertMesCode(mobile,mesCode,status,gmtCreate,gmtModified);
        }
        LOGGER.info("成功生成短信验证码 mesCode={}",mesCode);
        //发送验证码短信功能。。。
        ISMS smsService = new SMSService().getSMSImplPort();
        smsService.smsSend(mobile,"验证码:" + mesCode);
        LOGGER.info("结束检查并发送短信验证码方法");
    }
}
