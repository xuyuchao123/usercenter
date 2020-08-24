package com.xyc.userc.service.impl;

import com.xyc.userc.dao.BlacklistMapper;
import com.xyc.userc.dao.MobileOpenIdMapper;
import com.xyc.userc.entity.Blacklist;
import com.xyc.userc.entity.MobileOpenId;
import com.xyc.userc.service.BlacklistService;
import com.xyc.userc.util.BusinessException;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.vo.BlacklistVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by 1 on 2020/8/24.
 */
@Service("blacklistService")
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class BlacklistServiceImpl implements BlacklistService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(BlacklistServiceImpl.class);

    @Autowired
    private BlacklistMapper blacklistMapper;

    @Autowired
    private MobileOpenIdMapper mobileOpenIdMapper;

    @Override
    public List<BlacklistVo> getBlacklist(String name, String mobile,
                                          String createName, String createMobile) throws Exception
    {
        LOGGER.info("进入获取用户信息方法 name={} mobile={} createName={} createMobile={}",name,mobile,createName,createMobile);
        List<BlacklistVo> blacklistVoList = blacklistMapper.selectBlacklist(name,mobile,createName,createMobile);
        LOGGER.info("黑名单查询成功，查询结果：",blacklistVoList.toString());
        LOGGER.info("结束获取用户信息方法 name={} mobile={} createName={} createMobile={}",name,mobile,createName,createMobile);
        return blacklistVoList;
    }

    @Override
    public void addBlacklist(String mobile, String reason, String openId)
    {
        LOGGER.info("开始新增黑名单方法 mobile={} reason={} openId={}",mobile,reason,openId);
        //查询手机号绑定关系
        MobileOpenId mobileOpenId = null;
        mobileOpenId = mobileOpenIdMapper.selectByMobile(mobile);
        if(mobileOpenId == null)
        {
            LOGGER.info("未查到该手机号绑定关系,新增黑名单失败 mobile={}",mobile);
            throw new BusinessException(JsonResultEnum.INSERT_BLACKLIST_MOBILE_NOT_BINDED);
        }
        //检查该手机号是否已被拉黑
        List<Blacklist> blacklistList = blacklistMapper.selectByMobileOpenIdId(mobileOpenId.getId());
        if(blacklistList != null && blacklistList.size() > 0)
        {
            LOGGER.info("该手机号已被拉黑,新增黑名单失败 mobile={}",mobile);
            throw new BusinessException(JsonResultEnum.INSERT_BLACKLIST_MOBILE_EXIST);
        }
        Date date = new Date();
        Blacklist blacklist = new Blacklist(null,mobileOpenId.getId(),reason,openId,date,openId,date,1);
        int i = blacklistMapper.insertBlacklist(blacklist);
        LOGGER.info("结束新增黑名单方法 mobile={} reason={} openId={}",mobile,reason,openId);
    }

    @Override
    public void removeBlacklist(String mobile) throws Exception
    {
        LOGGER.info("开始删除黑名单方法 mobile={}",mobile);
        //查询手机号绑定关系
        MobileOpenId mobileOpenId = null;
        mobileOpenId = mobileOpenIdMapper.selectByMobile(mobile);
        if(mobileOpenId == null)
        {
            LOGGER.info("未查到该手机号绑定关系,删除黑名单失败 mobile={}",mobile);
            throw new BusinessException(JsonResultEnum.DELETE_BLACKLIST_MOBILE_NOT_BINDED);
        }
        blacklistMapper.deleteBlacklist(mobileOpenId.getId());
        LOGGER.info("结束删除黑名单方法 mobile={}",mobile);
    }


}
