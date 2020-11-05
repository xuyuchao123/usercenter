package com.xyc.userc.service.impl;

import com.xyc.userc.dao.BlacklistMapper;
import com.xyc.userc.dao.MobileOpenIdMapper;
import com.xyc.userc.entity.BlackListEnter;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public List<BlacklistVo> getBlacklist(String carNum, String createName, String createMobile, String page, String size) throws Exception
    {
        LOGGER.info("进入查询黑名单方法 carNum={} createName={} createMobile={} page={} size={}", carNum,createName,createMobile,page,size);
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
        int cnt = blacklistMapper.selectBlacklistCnt(carNum,createName,createMobile);
        int pageCnt = cnt / sizeInt + 1;
        if(pageInt > pageCnt)
        {
            pageInt = pageCnt;
        }
        int start = (pageInt - 1) * sizeInt + 1;
        int end = start + sizeInt - 1;
        LOGGER.info("查询黑名单信息 start={},end={},cnt={}",start,end,cnt);
        List<BlacklistVo> blacklistVoList = blacklistMapper.selectBlacklist(carNum,createName,createMobile,start,end);
        List resList = new ArrayList();
        resList.add(cnt);
        resList.add(pageInt);
        resList.add(sizeInt);
        resList.add(blacklistVoList);
        LOGGER.info("黑名单查询成功");
        LOGGER.info("结束查询黑名单方法 carNum={} createName={} createMobile={} page={} size={}",carNum,createName,createMobile,page,size);
        return resList;
    }

    @Override
    public void addBlacklist(String carNum, String reason, Long createUserId)
    {
        LOGGER.info("开始新增黑名单方法 carNum={} reason={} createUserId={}",carNum,reason,createUserId);
        //查询手机号绑定关系
//        MobileOpenId mobileOpenId = null;
//        mobileOpenId = mobileOpenIdMapper.selectByMobileOpenId(mobile,null);
//        if(mobileOpenId == null)
//        {
//            LOGGER.info("未查到该手机号绑定关系,新增黑名单失败 mobile={}",mobile);
//            throw new BusinessException(JsonResultEnum.MOBILE_NOT_BINDED);
//        }
        //检查该车牌号是否已被拉黑
        List<Blacklist> blacklistList = blacklistMapper.selectByCarNum(carNum);
        if(blacklistList != null && blacklistList.size() > 0)
        {
            LOGGER.info("该车牌号已被拉黑,新增黑名单失败 carNum={}",carNum);
            throw new BusinessException(JsonResultEnum.CARNUM_BLACKLIST_EXIST);
        }
        Timestamp date = new Timestamp(new Date().getTime());
        Blacklist blacklist = new Blacklist(null,carNum,reason,String.valueOf(createUserId),date,String.valueOf(createUserId),date,1);
        int i = blacklistMapper.insertBlacklist(blacklist);
        LOGGER.info("结束新增黑名单方法 carNum={} reason={} createUserId={}",carNum,reason,createUserId);
    }

    @Override
    public void removeBlacklist(String carNum) throws Exception
    {
        LOGGER.info("开始删除黑名单方法 carNum={}",carNum);
        //查询手机号绑定关系
//        MobileOpenId mobileOpenId = null;
//        mobileOpenId = mobileOpenIdMapper.selectByMobileOpenId(mobile,null);
//        if(mobileOpenId == null)
//        {
//            LOGGER.info("未查到该手机号绑定关系,删除黑名单失败 mobile={}",mobile);
//            throw new BusinessException(JsonResultEnum.MOBILE_NOT_BINDED);
//        }
        blacklistMapper.deleteBlacklist(carNum);
        LOGGER.info("结束删除黑名单方法 carNum={}",carNum);
    }

    @Override
    public void refreshBlackListEnter() throws Exception
    {
        LOGGER.info("进入查询黑名单车辆进场情况方法");
        List<BlackListEnter> blackListEnterList = blacklistMapper.selectBlacklistEnter();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //开始时间为当天0点
        String startTime = LocalDate.now() + " 00:00:00";
        //结束时间为当前时间
        String endTime = dateTimeFormatter.format(LocalDateTime.now());
        LOGGER.info("查询开始时间：{}，结束时间：{}",startTime,endTime);
        List<Map> maps = blacklistMapper.selectBlackListInInfo(startTime,endTime);
        LOGGER.info("结束查询黑名单车辆进场情况方法");


    }
}
