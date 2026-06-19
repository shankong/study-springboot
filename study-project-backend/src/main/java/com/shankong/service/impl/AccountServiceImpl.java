package com.shankong.service.impl;

import com.shankong.mapper.UserMapper;
import com.shankong.pojo.Account;
import com.shankong.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private VerificationCodeServiceImpl verificationCodeService;

    @Override
    public List<Account> showAccount() {
        List<Account> accountList = userMapper.showAccount();
        // 密码脱敏：返回给前端前把密码掩盖掉
        accountList.forEach(account -> account.setAccountPassword("******"));
        return accountList;
    }

    @Override
    public void deactivateByUsername(String username) {
       userMapper.deactivateByUsername(username);
    }

    @Override
    public Account findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        userMapper.updatePasswordByEmail(email, newPassword);
    }
}
