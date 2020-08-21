package com.xyc.userc.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by 1 on 2020/5/25.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.cors().and().csrf().disable();
        http.authorizeRequests()
                .anyRequest().authenticated()
            .and().formLogin()
                .loginProcessingUrl("/login")                       //登录form表单action的地址，即处理登录认证的请求地址
                .permitAll();                                        //允许所有用户
    }

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        super.configure(web);
        web.ignoring().antMatchers("/mes/**", "/error", "/v2/api-docs", "/swagger-resources/**", "/images/**",
                "/configuration/security", "/configuration/ui", "/swagger-ui.html", "/webjars/**", "/vendors/**","/MP_verify_M4vEkUcZh16Bd5Ly.txt");
    }
}
