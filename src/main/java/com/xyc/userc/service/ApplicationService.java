package com.xyc.userc.service;


import com.xyc.userc.entity.Application;
import java.util.List;

/**
 * Created by 1 on 2020/7/2.
 */
public interface ApplicationService
{
    List<Application> queryAllApplications() throws Exception;

    List<Application> queryApplication(String appId, String appName, String createUserNum,
                                       String createUsername) throws Exception;

}
