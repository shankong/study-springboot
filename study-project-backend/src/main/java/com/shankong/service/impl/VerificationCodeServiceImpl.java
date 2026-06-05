package com.shankong.service.impl;

import com.shankong.mapper.UserMapper;
import com.shankong.pojo.Account;
import com.shankong.service.VerificationCodeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Resource
    private UserMapper userMapper;

    // 内部类：验证码 + 过期时间
    private static class CodeEntry {
        String code;
        long expireTime;
        CodeEntry(String code, long expireTime) {
            this.code = code;
            this.expireTime = expireTime;
        }
    }

    // 内存存储：key=邮箱, value=验证码
    // ConcurrentHashMap多线程的线程安全Map，内置线程锁synchronized
    private final Map<String, CodeEntry> codeMap = new ConcurrentHashMap<>();

    /**
     * 生成6位数字验证码并保存
     */
    @Override
    public String generateCode(String email) {
        String code = String.format("%06d", new Random().nextInt(1000000));
        codeMap.put(email, new CodeEntry(code, System.currentTimeMillis() + 5 * 60 * 1000)); // 5分钟有效
        return code;

    }

    /**
     * 验证验证码是否正确且未过期
     */
    @Override
    public boolean verifyCode(String email, String code) {
        CodeEntry entry = codeMap.get(email);
        if (entry == null) return false;
        if (System.currentTimeMillis() > entry.expireTime) {
            codeMap.remove(email);
            return false;
        }
        return entry.code.equals(code);
    }

    /**
     * 验证通过后删除验证码（防止重复使用）
     */
    @Override
    public void removeCode(String email) {
        codeMap.remove(email);
    }

    /**
     * 更新数据库中的密码
     */
    @Override
    public void updatePassword(String email, String newPassword) {
        // UserMapper 目前没有 update 方法，需要加一个（见第7步）
        userMapper.updatePasswordByEmail(email, newPassword);
    }

    /**
     * 查看数据库有无该邮箱
     * @return true=邮箱已注册, false=邮箱未注册
     */
    @Override
    public boolean emailExists(String email) {
        Account account = userMapper.findByUsernameOrEmail(email);
        return account != null;
    }
}
