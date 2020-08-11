package com.xyc.userc.controller;

import com.avei.shriety.wx_sdk.pojo.ReturnData;
import com.avei.shriety.wx_sdk.template.Template;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 1 on 2020/8/11.
 */
@RestController
public class TemplateController
{
    @GetMapping("/mes/template")
    public ReturnData templateSend() {
        // oPh4us7phJe_qUG8o1TGY4Mrd2Yg 氕氘氚
        // oPh4us_Fe88F-HgRKn9QAYQTBzOs Pluto

        Map<String, String> propsMap = new HashMap<String, String>();
        propsMap.put("TOUSER", "oPh4us_Fe88F-HgRKn9QAYQTBzOs");
        propsMap.put("TEMPLATE_ID", "CMzvZBeSWTAYG7qp7B8CwTvc2tT4fwNQO8cQynQ2OCA");
        propsMap.put("FIRST", "测试数据");
        propsMap.put("KEYWORD1", "测试数据");
        propsMap.put("KEYWORD2", "测试数据");
        propsMap.put("KEYWORD3", "测试数据");
        propsMap.put("KEYWORD4", "测试数据");
        propsMap.put("REMARK", "测试数据");
        return Template.send(propsMap);
    }
}
