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
    public List<BlacklistVo> getBlacklist(String name, String mobile, String createName, String createMobile, String page, String size) throws Exception
    {
        LOGGER.info("进入查询黑名单方法 name={} mobile={} createName={} createMobile={} page={} size={}",
                name,mobile,createName,createMobile,page,size);
        if(page == null)
        {
            throw new BusinessException(JsonResultEnum.PAGE_NOT_EXIST);
        }
        if(size == null)
        {
            throw new BusinessException(JsonResultEnum.SIZE_NOT_EXIST);
        }

        int pageInt = Integer.valueOf(page);
        int sizeInt = Integer.valueOf(size);
        //查询总记录数
        int cnt = blacklistMapper.selectBlacklistCnt(name,mobile,createName,createMobile);
        int pageCnt = cnt / sizeInt + 1;
        if(pageInt > pageCnt)
        {
            pageInt = pageCnt;
        }
        int start = (pageInt - 1) * sizeInt + 1;
        int end = start + sizeInt - 1;
        LOGGER.info("查询黑名单信息 start={},end={},cnt={}",start,end,cnt);
        List<BlacklistVo> blacklistVoList = blacklistMapper.selectBlacklist(name,mobile,createName,createMobile);
        LOGGER.info("黑名单查询成功，查询结果：", blacklistVoList != null ? blacklistVoList.toString() : null);
        LOGGER.info("结束查询黑名单方法 name={} mobile={} createName={} createMobile={} page={} size={}",
                name,mobile,createName,createMobile,page,size);
        return blacklistVoList;
    }

    @Override
    public void addBlacklist(String mobile, String reason, Long createUserId)
    {
        LOGGER.info("开始新增黑名单方法 mobile={} reason={} createUserId={}",mobile,reason,createUserId);
        //查询手机号绑定关系
        MobileOpenId mobileOpenId = null;
        mobileOpenId = mobileOpenIdMapper.selectByMobileOpenId(mobile,null);
        if(mobileOpenId == null)
        {
            LOGGER.info("未查到该手机号绑定关系,新增黑名单失败 mobile={}",mobile);
            throw new BusinessException(JsonResultEnum.MOBILE_NOT_BINDED);
        }
        //检查该手机号是否已被拉黑
        List<Blacklist> blacklistList = blacklistMapper.selectByMobileOpenIdId(mobileOpenId.getId());
        if(blacklistList != null && blacklistList.size() > 0)
        {
            LOGGER.info("该手机号已被拉黑,新增黑名单失败 mobile={}",mobile);
            throw new BusinessException(JsonResultEnum.MOBILE_BLACKLIST_EXIST);
        }
        Date date = new Date();
        Blacklist blacklist = new Blacklist(null,mobileOpenId.getId(),reason,String.valueOf(createUserId),date,String.valueOf(createUserId),date,1);
        int i = blacklistMapper.insertBlacklist(blacklist);
        LOGGER.info("结束新增黑名单方法 mobile={} reason={} createUserId={}",mobile,reason,createUserId);
    }

    @Override
    public void removeBlacklist(String mobile) throws Exception
    {
        LOGGER.info("开始删除黑名单方法 mobile={}",mobile);
        //查询手机号绑定关系
        MobileOpenId mobileOpenId = null;
        mobileOpenId = mobileOpenIdMapper.selectByMobileOpenId(mobile,null);
        if(mobileOpenId == null)
        {
            LOGGER.info("未查到该手机号绑定关系,删除黑名单失败 mobile={}",mobile);
            throw new BusinessException(JsonResultEnum.MOBILE_NOT_BINDED);
        }
        blacklistMapper.deleteBlacklist(mobileOpenId.getId());
        LOGGER.info("结束删除黑名单方法 mobile={}",mobile);
    }


}
