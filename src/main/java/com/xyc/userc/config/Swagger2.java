package com.xyc.userc.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 2020/7/10.
 * swagger配置文件
 */
@Configuration
@EnableSwagger2
//是否开启swagger，为避免漏洞暴露正式环境一般需要关闭，可根据springboot的多环境配置进行设置
@ConditionalOnProperty(name = "swagger.enable",  havingValue = "true")
public class Swagger2
{
    // swagger2的配置文件，可以配置swagger2的一些基本的内容，比如扫描的包等等
    @Bean
    public Docket createRestApi()
    {
        //在配置好的配置类中增加此段代码即可
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        ticketPar.name("Authorization").description("登录校验")             //name表示名称，description表示描述
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).defaultValue("Bearer ").build();           //required表示是否必填，defaultvalue表示默认值
        pars.add(ticketPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.xyc.userc")).paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    // 构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    private ApiInfo apiInfo()
    {
        return new ApiInfoBuilder()
                // 页面标题
                .title("api列表信息描述")
                // 创建人信息
//                .contact(new Contact("MrZhang",  "https://www.cnblogs.com/zs-notes/category/1258467.html",  "1729497919@qq.com"))
                // 版本号
                .version("1.0")
                // 描述
//                .description("API 描述")
                .build();
    }
}
