package com.xyc;

import com.avei.shriety.wx_sdk.constant.WxsdkConstant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@MapperScan("com.xyc.userc.dao")	//使用双数据源时需要单独配置MapperScan，不能在此处全局配置
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.xyc", "com.avei.shriety.wx_sdk" })
@PropertySources({ @PropertySource(value = "classpath:wechat.properties", encoding = "UTF-8") })
public class UsercenterApplication {

	public static void main(String[] args) {
//		WxsdkConstant.ISPROXY = false;

		SpringApplication.run(UsercenterApplication.class, args);
	}

}
