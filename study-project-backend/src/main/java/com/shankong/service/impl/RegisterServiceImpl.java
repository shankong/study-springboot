package com.shankong.service.impl;

import com.shankong.mapper.UserMapper;
import com.shankong.pojo.Register;
import com.shankong.service.RegisterService;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean isEntryByUsername(String username) {
        return userMapper.findByUsernameOrEmail(username) != null;
    }

    @Override
    public Integer insertRegister(Register register) {
        register.setPassword(passwordEncoder.encode(register.getPassword()));
        return userMapper.insert(register);
    }
}
