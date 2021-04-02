package com.xyc.userc.service.impl;

import com.xyc.userc.service.ViolationService;
import org.springframework.stereotype.Service;

import com.xyc.userc.dao.ViolationMapper;
import com.xyc.userc.entity.Violation;
import com.xyc.userc.entity.ViolationDetail;
import com.xyc.userc.entity.ViolationInfo;
import com.xyc.userc.vo.ViolationInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 2020/10/22.
 */
@Service("violationService")
public class ViolationServiceImpl implements ViolationService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ViolationServiceImpl.class);

    @Autowired
    private ViolationMapper violationMapper;

    @Override
    public List<Violation> getAllViolation() throws Exception {
        LOGGER.info("进入查询违章大类方法");
        List<Violation> violations = violationMapper.selectAllViolation();
        LOGGER.info("结束查询违章大类方法");
        return violations;
    }

    @Override
    public List<ViolationDetail> getViolationDetail(Integer typeId) throws Exception {
        LOGGER.info("进入查询违章细类方法 typeId={}", typeId);
        List<ViolationDetail> violationDetails = violationMapper.selectViolationDetail(typeId);
        LOGGER.info("结束查询违章细类方法 typeId={}", typeId);
        return violationDetails;
    }

    @Override
    public void addViolationInfo(ViolationInfo violationInfo, MultipartFile violationImg) throws Exception {
        LOGGER.info("进入新增违章信息方法");
        InputStream inputStream = violationImg.getInputStream();
        byte[] bytes = new byte[(int) violationImg.getSize()];
        inputStream.read(bytes);
        violationInfo.setViolationImg(bytes);
        int insertCnt = violationMapper.insertViolationInfo(violationInfo);
        LOGGER.info("结束新增违章信息方法");
    }

    @Override
    public List<ViolationInfoVo> getViolationInfo(String billStaff, String billDep, String billTime, String carNumber, String paymentStatus) throws Exception {
        LOGGER.info("进入查询违章信息方法");
        if (billTime != null) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-MM-dd");
            billTime = df.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(billTime)), ZoneId.systemDefault()));
            LOGGER.info("billTime:{}", billTime);
        }
        List<ViolationInfoVo> violationInfoVos = violationMapper.selectViolationInfo(billStaff, billDep, billTime, carNumber, paymentStatus);
        LOGGER.info("结束查询违章信息方法");
        return violationInfoVos;
    }

    @Override
    public byte[] getViolationImg(Integer id) throws Exception {
        LOGGER.info("进入查询违章图片方法");
        Map<String, Object> map = violationMapper.selectViolationImg(id);
        byte[] bytes = (byte[]) map.get("violationImg");
        LOGGER.info("结束查询违章图片方法");
        return bytes;
    }
}
