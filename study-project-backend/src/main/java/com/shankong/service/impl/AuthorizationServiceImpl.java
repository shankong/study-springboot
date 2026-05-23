package com.shankong.service.impl;

import com.shankong.mapper.UserMapper;
import com.shankong.pojo.Account;
import com.shankong.service.AuthorizationService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    //跟@Autowired基本一样
    @Resource
    UserMapper userMapper;
    
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        if(usernameOrEmail == null) throw new UsernameNotFoundException("用户名或邮箱不能为空");
        Account byUsernameOrEmail = userMapper.findByUsernameOrEmail(usernameOrEmail);
        if(byUsernameOrEmail == null) throw new UsernameNotFoundException("用户名或密码错误");
        return User
                .withUsername(byUsernameOrEmail.getUsername()) // ① 静态方法，创建一个建造器，填入用户名
                .password(byUsernameOrEmail.getPassword()) // ② 填入密码（必须是hash加密后的）
                .roles("user")
                .build();
    }
}
