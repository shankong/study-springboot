package com.shankong.service;

import com.shankong.pojo.Register;

public interface RegisterService {
    boolean isEntryByUsername(String username);

    Integer insertRegister(Register register);
}
