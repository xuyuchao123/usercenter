package com.xyc.userc.service.impl;

import com.xyc.userc.dao.HallReportMapper;
import com.xyc.userc.entity.HallReportComment;
import com.xyc.userc.entity.HallReportInfo;
import com.xyc.userc.service.HallReportService;
import com.xyc.userc.util.BusinessException;
import com.xyc.userc.vo.HallReportCommentVo;
import com.xyc.userc.vo.HallReportInfoVo;
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
        HallReportInfo hallReportInfo = new HallReportInfo(null,openId,mobile,carNum,date,1,null,0,"",date);
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

    @Override
    public HallReportInfoVo getHallReportInfo(String openId) throws Exception
    {
        LOGGER.info("进入查询大厅报道信息方法 openId={}",openId);
        List<HallReportInfo> hallReportInfoList = hallReportMapper.selectHallReportInfo(openId,null,null,null);
        if(hallReportInfoList == null || hallReportInfoList.size() == 0)
        {
            LOGGER.error("大厅报道信息不存在 openId={}",openId);
            throw new BusinessException("大厅报道信息不存在!");
        }
        HallReportInfo hallReportInfo = hallReportInfoList.get(0);
        HallReportInfoVo hallReportInfoVo = new HallReportInfoVo(hallReportInfo.getId(),hallReportInfo.getMobile(),
                hallReportInfo.getCarNumber(),hallReportInfo.getLateTimes());
        LOGGER.info("结束查询当前被叫到的序号方法 openId={}",openId);
        return hallReportInfoVo;
    }

    @Override
    public HallReportCommentVo addHallReportComment(String openId, String carNum, String comment, String bigLadingBillNo) throws Exception
    {
        LOGGER.info("进入新增大厅报道评论方法 openId={} carNum={} bigLadingBillNo={}",openId,carNum,bigLadingBillNo);
        Date date = new Date();
        HallReportComment hallReportComment = new HallReportComment(null,openId,carNum,comment,date,bigLadingBillNo);
        hallReportMapper.insertComment(hallReportComment);
        HallReportCommentVo hallReportCommentVo = new HallReportCommentVo();
        hallReportCommentVo.setComment(comment);
        hallReportCommentVo.setGmtCreate(date);
        LOGGER.info("结束新增大厅报道评论方法 openId={} carNum={} bigLadingBillNo={}",openId,carNum,bigLadingBillNo);
        return hallReportCommentVo;
    }
}
