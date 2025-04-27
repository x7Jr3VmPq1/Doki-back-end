package com.megrez.dokibackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // 禁用 CSRF 防护（如不需要）
                .authorizeRequests()
                .anyRequest().permitAll()  // 所有请求不需要认证
                .and()
                .formLogin().disable()  // 禁用默认表单登录
                .httpBasic().disable(); // 禁用默认的基本认证
    }
}
