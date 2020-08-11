package com.xyc.userc.controller;

import com.avei.shriety.wx_sdk.constant.WxsdkConstant;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 1 on 2020/8/11.
 */
@Controller
public class UserInfoController
{
    @GetMapping("/mes/index")
    public void index(HttpServletRequest request, HttpServletResponse response)
    {
        System.out.println(request.getSession().getAttribute(WxsdkConstant.USERINFO));
    }
}
