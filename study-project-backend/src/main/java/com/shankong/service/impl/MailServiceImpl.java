package com.shankong.service.impl;

import com.shankong.service.MailService;
import jakarta.annotation.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    @Resource
    private JavaMailSender mailSender;

    /**
     * 发送验证码邮件
     */
    @Override
    public void sendVerificationCode(String toEmail, String code, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2197902558@qq.com");     // 发件人，和配置里一致
        message.setTo(toEmail);                    // 收件人
        message.setSubject(subject);               // 邮件标题，由调用方决定
        message.setText("您的验证码是：" + code + "，有效期5分钟。");  // 邮件内容
        mailSender.send(message);
    }
}
