package com.shankong.service;

import com.shankong.pojo.Account;

import java.util.List;

public interface AccountService {
    List<Account> showAccount();

    void deactivateByUsername(String username);

    Account findByUsername(String username);

    void updatePassword(String email, String newPassword);
}
