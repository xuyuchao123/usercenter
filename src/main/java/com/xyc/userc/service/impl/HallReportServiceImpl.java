package com.xyc.userc.service.impl;

import com.xyc.userc.dao.HallReportMapper;
import com.xyc.userc.entity.HallReportInfo;
import com.xyc.userc.service.HallReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by 1 on 2021/2/1.
 */
@Service("hallReportService")
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class HallReportServiceImpl implements HallReportService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(HallReportServiceImpl.class);

    @Autowired
    private HallReportMapper hallReportMapper;

    @Override
    public int addReportInfo(String openId, String mobile, String carNum) throws Exception
    {
        LOGGER.info("进入新增物流大厅报道记录方法 openid={} mobile={} carNum={}",openId,mobile,carNum);
        int dataStatus = 1;
        List<HallReportInfo> hallReportInfoList = hallReportMapper.selectHallReportInfo(openId,mobile,carNum,dataStatus);
        if(hallReportInfoList != null && hallReportInfoList.size() == 1)
        {
            int id = hallReportInfoList.get(0).getId();
            LOGGER.info("报道记录已存在 id={}",id);
            return id;
        }
        Date date = new Date();
        HallReportInfo hallReportInfo = new HallReportInfo(null,openId,mobile,carNum,date,1,null,0,"");
        hallReportMapper.insert(hallReportInfo);
        int id = hallReportInfo.getId();
        LOGGER.info("初始化列表序号：id={}",id);
        LOGGER.info("结束新增物流大厅报道记录方法 openid={} mobile={} carNum={}",openId,mobile,carNum);
        return id;
    }

    @Override
    public int getWaitingNum(String openId) throws Exception
    {
        LOGGER.info("进入查询等待人数方法 openid={}",openId);
        int waitingNum = hallReportMapper.selectWaitingNum(openId);
        LOGGER.info("结束查询等待人数方法 openid={} waitingNum={}",openId,waitingNum);
        return waitingNum;
    }

    @Override
    public int getCurrentNum() throws Exception
    {
        LOGGER.info("进入查询当前被叫到的序号方法");
        int curNum = 0;
        curNum = hallReportMapper.selectCurrentNum();
        LOGGER.info("结束查询当前被叫到的序号方法 curNum = {}",curNum);
        return curNum;
    }
}
