package com.xyc.userc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by 1 on 2021/4/2.
 */
@Configuration
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter
{

    @Value("${file.rootPath}")
    private String fileRootPath;

    @Value("${file.sonPath}")
    private String fileSonPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        String filePath = fileRootPath + fileSonPath;
        String filePath2 = "file:" + fileRootPath + fileSonPath;

        //指向外部目录
        registry.addResourceHandler("/static/violationimg/**").addResourceLocations(filePath,filePath2);
        super.addResourceHandlers(registry);
    }

}