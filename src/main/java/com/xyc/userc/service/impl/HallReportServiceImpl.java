package com.xyc.userc.service.impl;

import com.xyc.userc.dao.HallReportMapper;
import com.xyc.userc.entity.HallReportComment;
import com.xyc.userc.entity.HallReportInfo;
import com.xyc.userc.entity.QRCodeStrInfo;
import com.xyc.userc.service.HallReportService;
import com.xyc.userc.util.BusinessException;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.UsercConstant;
import com.xyc.userc.vo.HallReportCommentVo;
import com.xyc.userc.vo.HallReportInfoVo;
import com.xyc.userc.vo.HallReportPrintQueueVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    public List<String> addReportInfo(String openId, String mobile, String carNum, String bigLadingBillNo) throws Exception
    {
        LOGGER.info("进入新增物流大厅报道记录方法 openid={} mobile={} carNum={} bigLadingBillNo={}",openId,mobile,carNum,bigLadingBillNo);
        if(bigLadingBillNo == null || "".equals(bigLadingBillNo))
        {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String timeStr = dateTimeFormatter.format(LocalDateTime.now());
            LOGGER.info("提单号参数为空,开始查询提单号 carNum={} timeStr",carNum,timeStr);
            List<String> bigLadingBillNos = hallReportMapper.selectBigLadingBillNo(carNum,mobile,timeStr);
            if(bigLadingBillNos == null || bigLadingBillNos.size() == 0)
            {
                LOGGER.error("提单号不存在 carNum={} timeStr",carNum,timeStr);
                throw new BusinessException(JsonResultEnum.BIGLADINGBILLNO_NOT_EXIST);
            }
            if(bigLadingBillNos.size() > 1)
            {
                LOGGER.info("存在多个提单号,返回提单号列表 carNum={} timeStr",carNum,timeStr);
                return bigLadingBillNos;
            }
            else
            {
                LOGGER.info("只有一个提单号,准备新增物流大厅报道记录 carNum={} timeStr",carNum,timeStr);
                bigLadingBillNo = bigLadingBillNos.get(0);
            }
        }
        int dataStatus = 1;
        List<HallReportInfo> hallReportInfoList = hallReportMapper.selectHallReportInfo(openId,mobile,carNum,bigLadingBillNo,dataStatus);
        if(hallReportInfoList != null && hallReportInfoList.size() == 1)
        {
            int id = hallReportInfoList.get(0).getId();
            LOGGER.info("报道记录已存在 id={}",id);
            List<String> list = new ArrayList<>();
            list.add(String.valueOf(id));
            return list;
        }
        Date date = new Date();
        HallReportInfo hallReportInfo = new HallReportInfo(null,openId,mobile,carNum,date,0,0,0,bigLadingBillNo,null);
        hallReportMapper.insert(hallReportInfo);
        int id = hallReportInfo.getId();
        LOGGER.info("初始化列表序号：id={}",id);
        LOGGER.info("结束新增物流大厅报道记录方法 openid={} mobile={} carNum={} bigLadingBillNo={}",openId,mobile,carNum,bigLadingBillNo);
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(id));
        return list;
    }

    @Override
    public int getWaitingNum(String openId) throws Exception
    {
        LOGGER.info("进入查询等待人数方法 openid={}",openId);
        int waitingNum = 0;
        List<Map> maps = hallReportMapper.selectWaitingNum(openId);
        if(maps != null && maps.size() > 0)
        {
            for(int i = 0; i < maps.size(); i++)
            {
                if(openId.equals((String)maps.get(i).get("openId")))
                {
                    waitingNum = i;
                    break;
                }
            }
        }
        LOGGER.info("结束查询等待人数方法 openid={} waitingNum={}",openId,waitingNum);
        return waitingNum;
    }

    @Override
    public List<Integer> getCurrentNum() throws Exception
    {
        LOGGER.info("进入查询当前被叫到的序号方法");
        List<Integer> curNums = hallReportMapper.selectCurrentNum();
        LOGGER.info("结束查询当前被叫到的序号方法");
        return curNums;
    }

    @Override
    public HallReportInfoVo getHallReportInfo(String openId) throws Exception
    {
        LOGGER.info("进入查询大厅报道信息方法 openId={}",openId);
        List<HallReportInfo> hallReportInfoList = hallReportMapper.selectHallReportInfo(openId,null,null,null,null);
        if(hallReportInfoList == null || hallReportInfoList.size() == 0)
        {
            LOGGER.error("大厅报道信息不存在 openId={}",openId);
            throw new BusinessException("大厅报道信息不存在!");
        }
        HallReportInfo hallReportInfo = hallReportInfoList.get(0);
        HallReportInfoVo hallReportInfoVo = new HallReportInfoVo(hallReportInfo.getId(),hallReportInfo.getMobile(),
                hallReportInfo.getCarNumber(),hallReportInfo.getLateTimes());
        LOGGER.info("结束查询大厅报道信息方法 openId={}",openId);
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

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        String dateStr = dateTimeFormatter.format(localDateTime);
        hallReportCommentVo.setGmtCreate(dateStr);
        LOGGER.info("结束新增大厅报道评论方法 openId={} carNum={} bigLadingBillNo={}",openId,carNum,bigLadingBillNo);
        return hallReportCommentVo;
    }

    @Override
    public void refreshQRCodeStr() throws Exception
    {
        LOGGER.info("进入更新玖厅报道二维码字符串方法");
        int delCnt = hallReportMapper.deleteEarliestQRCodeStr();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        QRCodeStrInfo qrCodeStrInfo = new QRCodeStrInfo();
        qrCodeStrInfo.setqRCodeStr(UsercConstant.QRCODESTR_JL + uuid);
        qrCodeStrInfo.setGmtCreate(Timestamp.valueOf(LocalDateTime.now()).getTime());
        hallReportMapper.insertQRCodeStr(qrCodeStrInfo);
        LOGGER.info("结束更新玖厅报道二维码字符串方法");

    }

    @Override
    public List<QRCodeStrInfo> getQRCodeStr() throws Exception
    {
        LOGGER.info("进入获取玖隆大厅报道二维码字符串方法");
        List<QRCodeStrInfo> qrCodeStrInfoList = hallReportMapper.selectQRCodeStr();
        LOGGER.info("结束获取玖隆大厅厅报道二维码字符串方法");
        return qrCodeStrInfoList;
    }

    @Override
    public Map<String,List<HallReportPrintQueueVo>> getReportQueue() throws Exception
    {
        LOGGER.info("进入获取玖隆大厅报道及打印队列方法");
        List<HallReportPrintQueueVo> hallReportPrintQueueVoList = hallReportMapper.selectReportPrintQueue();
        List<HallReportPrintQueueVo> waitQueueVoList = new ArrayList<>();
        List<HallReportPrintQueueVo> printQueueList = new ArrayList<>();
        HallReportPrintQueueVo hallReportPrintQueueVo;
//        long curTimeStamp = Timestamp.valueOf(LocalDateTime.now()).getTime();
        int waitNo = 1;
        int printNo = 1;
        for(int i = 0; i < hallReportPrintQueueVoList.size(); i++)
        {
            if(waitQueueVoList.size() + printQueueList.size() == 30)
            {
                break;
            }
            hallReportPrintQueueVo = hallReportPrintQueueVoList.get(i);
            if("waiting".equals(hallReportPrintQueueVo.getStatus()))
            {
                if(waitQueueVoList.size() >= 15)
                {
                    continue;
                }
                hallReportPrintQueueVo.setNo(waitNo++);
                if(hallReportPrintQueueVo.getTimeout() != null)
                {
                    hallReportPrintQueueVo.setTimeout(null);
                }
                waitQueueVoList.add(hallReportPrintQueueVo);
            }
            else
            {
                if(printQueueList.size() >= 15)
                {
                    continue;
                }
                hallReportPrintQueueVo.setNo(printNo++);
                hallReportPrintQueueVo.setTimeout(hallReportPrintQueueVo.getTimeout().longValue() + 900000);
                printQueueList.add(hallReportPrintQueueVo);
            }
        }
        Map<String,List<HallReportPrintQueueVo>> map = new HashMap<>();
        map.put("waitingQueue",waitQueueVoList);
        map.put("printingQueue",printQueueList);
        LOGGER.info("结束获取玖隆大厅报道及打印队列方法");
        return map;
    }
}
