package com.xyc.userc.service;

/**
 * Created by 1 on 2020/7/8.
 */
public interface MobileService
{
    public void checkAndSendMesCode(String mobile) throws Exception;



    public String loadPcMesCodeByMobile(String mobile) throws Exception;

    public String addMesCode(String mobile, String mesCode) throws Exception;

    public void setMesCodeInvalid(String mobile) throws Exception;



}
