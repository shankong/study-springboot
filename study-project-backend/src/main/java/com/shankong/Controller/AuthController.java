package com.shankong.Controller;

import com.shankong.entity.RestBean;
import com.shankong.pojo.Account;
import com.shankong.service.AccountService;
import com.shankong.service.MailService;
import com.shankong.service.VerificationCodeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 忘记密码、注销账号、修改密码、获取当前用户信息
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

    @Resource
    private AccountService accountService;

    @Resource
    private PersistentTokenRepository persistentTokenRepository; //持久化 token

    /**
     * 查看所有用户（密码已脱敏）
     */
    @GetMapping("/showAccount")
    public RestBean<List<Account>> showAccount() {
        List<Account> list = accountService.showAccount();
        return RestBean.success(list);
    }

    /**
     * 获取当前登录用户信息（右上角显示用户名用）
     */
    @GetMapping("/me")
    public RestBean<?> me() {
        // 从 Spring Security 拿当前登录的用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findByUsername(username);
        if (account == null) {
            return RestBean.failure(400, "用户不存在");
        }
        // 返回前把密码脱敏
        account.setAccountPassword("******");
        return RestBean.success(account);
    }
/**==========================修改密码============================================================================*/
    /**
     * 修改密码-第一步：发送验证码到当前用户邮箱
     */
    @PostMapping("/change-password/send-code")
    public RestBean<String> sendChangePasswordCode() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findByUsername(username);
        if (account == null) {
            return RestBean.failure(400, "用户不存在");
        }
        String code = verificationCodeService.generateCode(account.getAccountEmail());
        mailService.sendVerificationCode(account.getAccountEmail(), code, "图书管理系统 - 修改密码验证码");
        return RestBean.success("验证码已发送至 " + account.getAccountEmail());
    }

    /**
     * 修改密码-第二步：验证验证码，更新密码
     */
    @PostMapping("/change-password")
    public RestBean<String> changePassword(@RequestParam String code,
                                            @RequestParam String password,
                                            HttpServletRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findByUsername(username);
        if (account == null) {
            return RestBean.failure(400, "用户不存在");
        }
        // 验证验证码
        if (!verificationCodeService.verifyCode(account.getAccountEmail(), code)) {
            return RestBean.failure(400, "验证码错误或已过期");
        }
        // 加密新密码并更新
        accountService.updatePassword(account.getAccountEmail(), passwordEncoder.encode(password));
        verificationCodeService.removeCode(account.getAccountEmail());

        // 密码已改，清除登录状态，让用户用新密码重新登录
        persistentTokenRepository.removeUserTokens(username);
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return RestBean.success("密码修改成功");
    }
/**======================注销账号=======================================================================*/
    /**
     * 注销账号-第一步：发送验证码到当前用户邮箱
     */
    @PostMapping("/deactivate/send-code")
    public RestBean<String> sendDeactivateCode() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findByUsername(username);
        if (account == null) {
            return RestBean.failure(400, "用户不存在");
        }
        String code = verificationCodeService.generateCode(account.getAccountEmail());
        mailService.sendVerificationCode(account.getAccountEmail(), code, "图书管理系统 - 账号注销验证码");
        return RestBean.success("验证码已发送至 " + account.getAccountEmail());
    }

    /**
     * 注销账号-第二步：验证验证码，软删除账号（account_state=2）
     */
    @PostMapping("/deactivate")
    public RestBean<String> deactivate(@RequestParam String code,
                                        HttpServletRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findByUsername(username);
        if (account == null) {
            return RestBean.failure(400, "用户不存在");
        }
        // 验证验证码
        if (!verificationCodeService.verifyCode(account.getAccountEmail(), code)) {
            return RestBean.failure(400, "验证码错误或已过期");
        }
        // 软删除：account_state 设为 2
        accountService.deactivateByUsername(username);
        verificationCodeService.removeCode(account.getAccountEmail());

        // 清除登录状态，让用户回到登录页
        persistentTokenRepository.removeUserTokens(username);     // 1. 删掉 remember-me 持久化 token
        SecurityContextHolder.clearContext();                     // 2. 清除安全上下文
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();                                 // 3. 销毁 session
        }

        return RestBean.success("账号已注销");
    }
/**======================忘记密码=======================================================================*/

    /**
     * 忘记密码-第一步：用户提交邮箱 → 发送验证码
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
     * 忘记密码-第二步：用户提交验证码 + 新密码 → 重置密码
     */
    @PostMapping("/reset-password")
    public RestBean<String> resetPassword(@RequestParam String email,
                                          @RequestParam String code,
                                          @RequestParam String password,
                                          HttpServletRequest request) {
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
