package com.xyc.userc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by 1 on 2021/4/2.
 */
@Configuration
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter
{

//    //图片存放根路径
//    @Value("${file.rootPath}")
//    private String ROOT_PATH;
//    //图片存放根目录下的子目录
//    @Value("${file.sonPath}")
//    private String SON_PATH;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        String filePath = "file:" + "E:" + "/violationimg/";
        //指向外部目录
        registry.addResourceHandler("/static/violationimg//**").addResourceLocations(filePath);
        super.addResourceHandlers(registry);
    }

}