package com.shankong.service;

public interface MailService {
    /**
     * 发送验证码邮件
     * @param toEmail 收件人邮箱
     * @param code 验证码
     * @param subject 邮件标题
     */
    void sendVerificationCode(String toEmail, String code, String subject);
}
