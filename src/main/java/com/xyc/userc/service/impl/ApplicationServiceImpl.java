package com.xyc.userc.service.impl;

import com.xyc.userc.dao.ApplicationMapper;
import com.xyc.userc.entity.Application;
import com.xyc.userc.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 1 on 2020/7/2.
 */
@Service("applicationService")
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class ApplicationServiceImpl implements ApplicationService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    @Autowired
    ApplicationMapper applicationMapper;

    //查询所有已注册系统
    @Override
    public List<Application> queryAllApplications() throws Exception
    {
        LOGGER.debug("进入查询所有已注册系统信息方法");
        List<Application> applicationList = applicationMapper.selectAll();
        LOGGER.debug("结束查询所有已注册系统信息方法");
        return applicationList;
    }

    @Override
    public List<Application> queryApplication(String appId, String appName, String createUserNum,
                                              String createUsername) throws Exception
    {
        LOGGER.debug("进入按条件查询已注册系统信息方法");
        byte isDeleted = 0;
        List<Application> applicationList = applicationMapper.selectApplication(appId,appName,
                createUsername,createUserNum,isDeleted);
        LOGGER.debug("结束按条件查询所有已注册系统信息方法");
        return applicationList;
    }
}
