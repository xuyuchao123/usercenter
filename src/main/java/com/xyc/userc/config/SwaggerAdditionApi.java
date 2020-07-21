package com.xyc.userc.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;
import com.xyc.userc.util.JsonResultObj;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.*;

/**
 * Created by 1 on 2020/7/14.
 * 自定义swagger接口说明文档，（由于系统用户登录注销操作由springSecurity代管，无法用swagger注解自动生成接口文档）
 */
@Component
public class SwaggerAdditionApi implements ApiListingScannerPlugin
{

    @Override
    public List<ApiDescription> apply(DocumentationContext documentationContext)
    {
        return new ArrayList<ApiDescription>
        (
            Arrays.asList
            (
                new ApiDescription
                (
                    "/login",  //url
                    "用户登录", //描述
                    Arrays.asList
                    (
                        new OperationBuilder(new CachingOperationNameGenerator())
                            .method(HttpMethod.POST)//http请求类型
                            .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                            .summary("用户登录")
//                            .notes("用户登录3")//方法描述
                            .tags(Sets.newHashSet("系统用户登入登出相关api"))//归类标签
                            .parameters
                            (
                                Arrays.asList
                                (
                                    new ParameterBuilder()
                                        .description("用户名")//参数描述
                                        .type(new TypeResolver().resolve(String.class))//参数数据类型
                                        .name("username")//参数名称
//                                        .defaultValue("password")//参数默认值
                                        .parameterType("query")//参数类型
                                        .parameterAccess("access")
                                        .required(true)//是否必填
                                        .modelRef(new ModelRef("string")) //参数数据类型
                                        .order(1)
                                        .build(),
                                    new ParameterBuilder()
                                        .description("密码")
                                        .type(new TypeResolver().resolve(String.class))
                                        .name("password")
                                        .parameterType("query")
                                        .parameterAccess("access")
                                        .required(true)
                                        .modelRef(new ModelRef("string"))
                                        .order(2)
                                        .build(),
                                    new ParameterBuilder()
                                        .description("登录类型")
                                        .type(new TypeResolver().resolve(String.class))
                                        .name("type")
                                        .defaultValue("account/mobile")
                                        .parameterType("query")
                                        .parameterAccess("access")
                                        .required(true)
                                        .modelRef(new ModelRef("string"))
                                        .order(3)
                                        .build()
                                )
                            )
                            .responseMessages(loginResponseMessages())
                            .build()
                    ),
                    false
                ),
                new ApiDescription
                (
                    "/logout",  //url
                    "用户退出登录", //描述
                    Arrays.asList
                    (
                        new OperationBuilder(new CachingOperationNameGenerator())
                            .method(HttpMethod.GET)//http请求类型
                            .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                            .summary("用户退出登录")
//                            .notes("用户登录3")//方法描述
                            .tags(Sets.newHashSet("系统用户登入登出相关api"))//归类标签
                            .responseMessages(logoutResponseMessages())
                            .build()
                    ),
                    false
                )
            )
        );
    }

    /**
     * 登录接口响应信息集合
     */
    private Set<ResponseMessage> loginResponseMessages()
    {
        return Collections.singleton
        (
            new ResponseMessageBuilder()
                .code(200)
                .message("isSuccess=true：登录成功，resData返回用户信息对象 isSuccess=false：登录失败，resMsg为错误信息，resData为空")
                .responseModel
                (
                    new ModelRef
                    (
                        JsonResultObj.class.getSimpleName()
                    )
                )
                .build()
        );
    }

    /**
     * 退出登录接口响应信息集合
     */
    private Set<ResponseMessage> logoutResponseMessages()
    {
        return Collections.singleton
        (
            new ResponseMessageBuilder()
                .code(200)
                .message("isSuccess=true：退出登录成功 isSuccess=false：退出登录失败，resMsg为错误信息")
                .responseModel
                (
                    new ModelRef
                    (
                        JsonResultObj.class.getSimpleName()
                    )
                )
                .build()
        );
    }


    @Override
    public boolean supports(DocumentationType documentationType)
    {
        return DocumentationType.SWAGGER_2.equals(documentationType);
    }
}
