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
import java.util.*;

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
        LOGGER.info("进入刷新黑名单车辆进场情况方法");
        List<BlackListEnter> blackListEnterList = new ArrayList<>();
        blackListEnterList = blacklistMapper.selectBlacklistEnter();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String curDate = LocalDate.now() + "";
        //开始时间为当天0点
        String startTime = curDate + " 00:00:00";
        //结束时间为当前时间
        String endTime = dateTimeFormatter.format(LocalDateTime.now());
        LOGGER.info("查询开始时间：{}，结束时间：{}",startTime,endTime);
        List<Map> maps = new ArrayList<>();
        //查询从当天0点开始到当前时间点，黑名单车牌号对应的车辆的进场情况
        maps = blacklistMapper.selectBlackListInInfo(startTime,endTime);
        Map<String,String> carNumCrosDateMap = new HashMap<>();
        //将车牌号作为 key，当天日期作为 value（若车辆没有进场记录，则 value 值为' '） 放入 carNumCrosDateMap 中
        for(Map map : maps)
        {
            carNumCrosDateMap.put(map.get("CARNUM").toString(),map.get("CROSSDATE").toString());
        }
        List<String> uptCurDateValidStatList = new ArrayList<>();
        List<String> uptStatInvalidList = new ArrayList<>();
//        List<String> insertList = new ArrayList<>();
        //遍历黑名单车牌号对应车辆当日进场情况表查到的记录
        for(BlackListEnter blackListEnter : blackListEnterList)
        {
            String tmpCarNum = blackListEnter.getCarNum();
            //若 carNumCrosDateMap 中存在当前遍历的车牌号，则判断 blackListEnter 的 enterDate 及 status 属性的值
            if(carNumCrosDateMap.containsKey(tmpCarNum))
            {
                //若 enterDate 不等于当天日期，或者 status 为无效，则将对应车牌号放入 uptCurDateValidStatList 中
                if(!curDate.equals(blackListEnter.getEnterDate()) || blackListEnter.getStatus() != 1)
                {
                    uptCurDateValidStatList.add(tmpCarNum);
                }
                //将当前车牌号从 carNumCrosDateMap 中移除
                carNumCrosDateMap.remove(tmpCarNum);
            }
            //若 carNumCrosDateMap 中不存存在当前遍历的车牌号,说明该车牌号已从黑名单中移除
            else
            {
                //若 blackListEnter 的 status 属性值为有效，则放入 uptStatInvalidList 中
                if(blackListEnter.getStatus() == 1)
                {
                    uptStatInvalidList.add(tmpCarNum);
                }
            }
        }
        //该 list 中为需要更新进场日期和数据状态为有效的车牌号
        if(uptCurDateValidStatList.size() > 0)
        {
            blacklistMapper.updateCurDateValidStat(uptCurDateValidStatList,curDate);
        }
        //该 list 中为需要更新数据状态为无效的车牌号
        if(uptStatInvalidList.size() > 0)
        {
            blacklistMapper.updateStatInvalid(uptStatInvalidList);
        }
        //将carNumCrosDateMap 中剩下的key，value 即为新增的黑名单车牌号，因此需要插入车辆当日进场情况表中
        if(carNumCrosDateMap.size() > 0)
        {
            for(Map.Entry<String, String> entry : carNumCrosDateMap.entrySet())
            {
                String key = entry.getKey();
                String value = entry.getValue();
                blacklistMapper.insertBlacklistEnter(key,value);
            }
        }
        LOGGER.info("结束刷新黑名单车辆进场情况方法");
    }
}
