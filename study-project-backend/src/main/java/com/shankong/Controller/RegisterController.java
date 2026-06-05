package com.shankong.Controller;

import com.shankong.entity.RestBean;
import com.shankong.pojo.Register;
import com.shankong.service.MailService;
import com.shankong.service.RegisterService;
import com.shankong.service.VerificationCodeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class RegisterController {

    @Resource
    private RegisterService registerService;

    @Resource
    private MailService mailService;

    @Resource
    private VerificationCodeService verificationCodeService;

    // 第一步：发送注册验证码
    @PostMapping("/user-register")
    public RestBean<String> register(@RequestBody Register register) {
        if(verificationCodeService.emailExists(register.getEmail())){
            return RestBean.failure(400, "邮箱已注册");
        }
        if(registerService.isEntryByUsername(register.getUsername())){
            return RestBean.failure(400, "用户名已存在");
        }

        // 2. 生成验证码
        String emailCode = verificationCodeService.generateCode(register.getEmail());
        // 3. 发邮件
        mailService.sendVerificationCode(register.getEmail(), emailCode, "图书管理系统 - 注册验证码");
        // 4. 返回成功
        return RestBean.success("验证码已发送");
    }

    //提交注册
    @PostMapping("/commit-register")
    public RestBean<String> commitRegister(@RequestBody Register register) {
        // 1. 验证码对不对
        if (!verificationCodeService.verifyCode(register.getEmail(), register.getEmailCode())) {
            return RestBean.failure(400, "验证码错误或已过期");
        }
        // 2. 插入数据库
        Integer insert = registerService.insertRegister(register);
        if (insert == null) {
            return RestBean.failure(400, "注册失败");
        }
        // 3. 删掉用过的验证码
        verificationCodeService.removeCode(register.getEmail());
        return RestBean.success("注册成功");
    }
}
