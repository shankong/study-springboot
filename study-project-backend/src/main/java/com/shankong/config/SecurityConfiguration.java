package com.shankong.config;

import com.alibaba.fastjson.JSONObject;
import com.shankong.entity.RestBean;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@Configuration //告诉 Spring：这是一个配置类，项目启动时会自动加载
@EnableWebSecurity //启用 Spring Security 的 Web 安全功能
public class SecurityConfiguration {

    @Bean
    //安全过滤器链，所有请求都会经过这个链上的过滤器
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                //过滤，只有登陆后才能访问
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                //表单登陆
                .formLogin(form -> form
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(this::onLoginSuccess)
                        .failureHandler(this::onLoginFailure)
                )
                //登出
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                )
                //没登录就去访问受保护的接口
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(this::onUnauthorized)
                )
                //关闭 CSRF 防护。如果你是前后端分离的 API 项目，通常要关掉，否则 POST 请求会被拦截
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    /**
     * 登录成功：返回 JSON
     */
    private void onLoginSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功")));
    }

    /**
     * 登录失败：返回 JSON
     */
    private void onLoginFailure(HttpServletRequest request,
                                HttpServletResponse response,
                                AuthenticationException exception) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401, "登录失败")));
    }

    /**
     * 未登录就访问受保护接口：返回 JSON，而不是重定向到登录页
     */
    private void onUnauthorized(HttpServletRequest request,
                                HttpServletResponse response,
                                AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(401);
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401, "未登录")));
    }
}
