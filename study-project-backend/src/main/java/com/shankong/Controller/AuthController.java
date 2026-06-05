package com.shankong.Controller;

import com.shankong.entity.RestBean;
import com.shankong.service.MailService;
import com.shankong.service.VerificationCodeService;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 忘记密码功能实现
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private MailService mailService;

    @Resource
    private VerificationCodeService verificationCodeService;

    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 第一步：用户提交邮箱 → 发送验证码
     */
    @PostMapping("/forgot-password")
    public RestBean<String> forgotPassword(@RequestParam String email) {
        // 1. 查数据库有没有这个邮箱

        if (!verificationCodeService.emailExists(email)) {
            return RestBean.failure(400, "该邮箱未注册");
        }
        // 2. 生成验证码
        String code = verificationCodeService.generateCode(email);
        // 3. 发邮件
        mailService.sendVerificationCode(email, code, "图书管理系统 - 密码重置验证码");
        // 4. 返回成功
        return RestBean.success("验证码已发送");
    }

    /**
     * 第二步：用户提交验证码 + 新密码 → 重置密码
     */
    @PostMapping("/reset-password")
    public RestBean<String> resetPassword(@RequestParam String email,
                                          @RequestParam String code,
                                          @RequestParam String password) {
        // 1. 验证码对不对
        if (!verificationCodeService.verifyCode(email, code)) {
            return RestBean.failure(400, "验证码错误或已过期");
        }
        // 2. 更新密码（加密后存进去）
        verificationCodeService.updatePassword(email, passwordEncoder.encode(password));
        // 3. 删掉用过的验证码
        verificationCodeService.removeCode(email);
        return RestBean.success("密码重置成功");
    }


}
