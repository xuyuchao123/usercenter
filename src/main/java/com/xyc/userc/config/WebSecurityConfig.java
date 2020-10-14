package com.xyc.userc.config;


import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.xyc.userc.security.*;
import com.xyc.userc.service.MobileService;
import com.xyc.userc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Properties;

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

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.cors().and().csrf().disable();
        http.authorizeRequests()
                .anyRequest().authenticated()
            .and().formLogin()
                .loginProcessingUrl("/login")                       //登录form表单action的地址，即处理登录认证的请求地址
                .permitAll()                                        //允许所有用户
                .successHandler(myAuthenticationSuccessHandler)     //登录成功处理逻辑
                .failureHandler(myAuthenticationFailureHandler)    //登录失败处理逻辑
            .and().logout()
                .permitAll().//允许所有用户
                logoutSuccessHandler(myLogoutSuccessHandler).       //登出成功处理逻辑
                deleteCookies("JSESSIONID").                        //登出之后删除cookie;
            and().rememberMe()                                      //开启自动登录功能
                .key("security")
                .tokenRepository(jdbcTokenRepository());            //开启持久化令牌

//        MesCodeAuthenticationFilter暂时不用
//        MesCodeAuthenticationFilter mesCodeAuthenticationFilter = new MesCodeAuthenticationFilter();
//        mesCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
//        mesCodeAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
//        mesCodeAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);

        MesCodeAuthenticationProvider mesCodeAuthenticationProvider = new MesCodeAuthenticationProvider();
        mesCodeAuthenticationProvider.setUserService(userService);
        mesCodeAuthenticationProvider.setMobileService(mobileService);

        http.authenticationProvider(mesCodeAuthenticationProvider);
//                .addFilterAfter(mesCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

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

    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;

    @Bean
    JdbcTokenRepositoryImpl jdbcTokenRepository()
    {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    DefaultKaptcha  verifyCode()
    {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width", "150");
        properties.setProperty("kaptcha.image.height", "50");
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    @Bean
    MyDaoAuthenticationProvider myDaoAuthenticationProvider()
    {
        MyDaoAuthenticationProvider myDaoAuthenticationProvider = new MyDaoAuthenticationProvider();
        myDaoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        myDaoAuthenticationProvider.setUserDetailsService(userService);
        return myDaoAuthenticationProvider;
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception
    {
        ProviderManager manager = new ProviderManager(Arrays.asList(myDaoAuthenticationProvider()));
        return manager;
    }
}
