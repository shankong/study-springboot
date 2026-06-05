package com.shankong.Controller;

import com.shankong.entity.RestBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class RememberController {
    /**
     * 检查用户是否已登录（包括"记住我"自动登录）
     * 前端路由守卫用这个接口判断要不要跳转到首页
     */
    @GetMapping("/status")
    public RestBean<String> status() {
        // 能走到这里说明 Spring Security 已经认证通过了（包括 remember-me）
        // 如果没登录，会被 SecurityFilterChain 拦截返回 401
        return RestBean.success("已登录");
    }
}
