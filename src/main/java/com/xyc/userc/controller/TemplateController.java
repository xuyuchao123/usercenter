package com.xyc.userc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avei.shriety.wx_sdk.constant.WxsdkConstant;
import com.avei.shriety.wx_sdk.pojo.ReturnData;
import com.avei.shriety.wx_sdk.template.Template;

/**
 * Created by 1 on 2020/8/11.
 */
@RestController
public class TemplateController {
	@GetMapping("/mes/template")
	public ReturnData templateSend() {
		// oPh4us7phJe_qUG8o1TGY4Mrd2Yg 氕氘氚
		// oPh4us_Fe88F-HgRKn9QAYQTBzOs Pluto
		// oPh4us__A3ShBKk7fkxu2ehE_OWI 喂喂

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

	@GetMapping("/mes/userinfo")
	public ReturnData userinfo(HttpSession session) {
		return new ReturnData(ReturnData.SUCCESS_CODE, session.getAttribute(WxsdkConstant.USERINFO));
	}
}
