package com.shankong.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

//Spring Security 只认 UserDetailsService 这个标准接口
public interface AuthorizationService extends UserDetailsService {
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException;
}
