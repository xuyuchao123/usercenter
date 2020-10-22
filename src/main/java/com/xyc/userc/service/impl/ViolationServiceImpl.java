package com.xyc.userc.service.impl;

import com.xyc.userc.dao.UserMapper;
import com.xyc.userc.dao.ViolationMapper;
import com.xyc.userc.entity.Violation;
import com.xyc.userc.entity.ViolationDetail;
import com.xyc.userc.service.ViolationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 1 on 2020/10/22.
 */
@Service("violationService")
public class ViolationServiceImpl implements ViolationService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(ViolationServiceImpl.class);

    @Autowired
    private ViolationMapper violationMapper;

    @Override
    public List<Violation> getAllViolation() throws Exception
    {
        LOGGER.info("进入查询违章大类方法");
        List<Violation> violations = violationMapper.selectAllViolation();
        LOGGER.info("结束查询违章大类方法");
        return violations;
    }

    @Override
    public List<ViolationDetail> getViolationDetail(Integer typeId) throws Exception
    {
        LOGGER.info("进入查询违章细类方法 typeId={}",typeId);
        List<ViolationDetail> violationDetails = violationMapper.selectViolationDetail(typeId);
        LOGGER.info("结束查询违章细类方法 typeId={}",typeId);
        return violationDetails;
    }


}
