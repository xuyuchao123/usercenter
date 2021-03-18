package com.xyc.userc.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

/**
 * Created by 1 on 2020/8/8.
 * 验证码控制器
 */
@Controller
@CrossOrigin
@RequestMapping("/mes")
//@Api(tags = "验证码相关api")
public class VerifyCodeController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(VerifyCodeController.class);

    @Resource
    DefaultKaptcha defaultKaptcha;

    /*
    * 获取验证码
    * */
    @RequestMapping(value = "/verifyCode.jpg",method = RequestMethod.GET)
//    @ResponseBody
//    @ApiOperation(value="获取验证码图片")
    public void getVerifyCode(HttpServletResponse response, HttpSession session)
    {
        LOGGER.debug("开始生成验证码图片");
        try
        {
            response.setContentType("image/jpeg");
            String text = defaultKaptcha.createText();
            session.setAttribute("verify_code", text);
            BufferedImage image = defaultKaptcha.createImage(text);
            try(ServletOutputStream out = response.getOutputStream())
            {
                ImageIO.write(image, "jpg", out);
            }
            LOGGER.debug("成功生成验证码图片");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("验证码图片生成失败：{}",e.getMessage());
        }
        LOGGER.debug("结束生成验证码");
    }
}
