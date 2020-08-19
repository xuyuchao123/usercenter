package com.xyc.userc.config;

import com.xyc.userc.security.*;
import com.xyc.userc.service.MobileService;
import com.xyc.userc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by 1 on 2020/5/25.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    //自定义用户认证类
    @Autowired
    UserService userService;

    @Autowired
    MobileService mobileService;

    //自定义用户未登录或访问无权限资源时异常处理类
    @Autowired
    MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    //登录成功处理类
    @Autowired
    MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    //登录失败处理类
    @Autowired
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    //退出登录成功处理类
    @Autowired
    MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Autowired
    MyAccessDecisionManager myAccessDecisionManager;

    @Autowired
    MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource;

//    @Autowired
//    private MyAbstractSecurityInterceptor myAbstractSecurityInterceptor;

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.cors().and().csrf().disable();
//        http.authorizeRequests().antMatchers("/mes/**", "/error", "/v2/api-docs", "/swagger-resources/**", "/images/**",
//                "/configuration/security", "/configuration/ui", "/swagger-ui.html", "/webjars/**").permitAll()
//                .anyRequest().authenticated()
        http.authorizeRequests()
                .anyRequest().authenticated()
//            withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                @Override
//                public <O extends FilterSecurityInterceptor> O postProcess(O o) {
//                    o.setAccessDecisionManager(myAccessDecisionManager);//决策管理器
//                    o.setSecurityMetadataSource(myFilterInvocationSecurityMetadataSource);//安全元数据源
//                    return o;
//                }
//            })

            .and().exceptionHandling()
                .authenticationEntryPoint(myAuthenticationEntryPoint)   //用户未登录的异常处理逻辑
            .and().formLogin()
                .loginProcessingUrl("/login")                       //登录form表单action的地址，即处理登录认证的请求地址
                .permitAll()                                        //允许所有用户
                .successHandler(myAuthenticationSuccessHandler)     //登录成功处理逻辑
                .failureHandler(myAuthenticationFailureHandler)    //登录失败处理逻辑
            .and().logout().
                    permitAll().//允许所有用户
                    logoutSuccessHandler(myLogoutSuccessHandler). //登出成功处理逻辑
                    deleteCookies("JSESSIONID");                //登出之后删除cookie;

        MesCodeAuthenticationFilter mesCodeAuthenticationFilter = new MesCodeAuthenticationFilter();
        mesCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        mesCodeAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        mesCodeAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);



        MyAbstractSecurityInterceptor myAbstractSecurityInterceptor = new MyAbstractSecurityInterceptor();
        myAbstractSecurityInterceptor.setMyAccessDecisionManager(myAccessDecisionManager);
        myAbstractSecurityInterceptor.setSecurityMetadataSource(myFilterInvocationSecurityMetadataSource);
        http.addFilterAfter(myAbstractSecurityInterceptor, FilterSecurityInterceptor.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        super.configure(web);
        web.ignoring().antMatchers("/mes/**", "/error", "/v2/api-docs", "/swagger-resources/**", "/images/**",
                "/configuration/security", "/configuration/ui", "/swagger-ui.html", "/webjars/**", "/vendors/**","/MP_verify_M4vEkUcZh16Bd5Ly.txt");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        // 设置加密方式（强hash方式）
        return new BCryptPasswordEncoder();
    }

}
