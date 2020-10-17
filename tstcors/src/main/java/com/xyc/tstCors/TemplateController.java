package com.xyc.tstCors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by 1 on 2020/10/17.
 */
@Controller
public class TemplateController
{
    //通用跳转方法
    @RequestMapping(value="/{viewName}",method= RequestMethod.GET)
    public String toProcOperate(@PathVariable("viewName") String viewName, ModelMap modelMap)
    {
//        System.out.println(modelMap.get("procInstId"));
        return viewName;
    }
}
