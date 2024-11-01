package me.deshark.lms.server.user.service.impl;


import jakarta.annotation.Resource;
import me.deshark.lms.server.user.LoginUserDetails;
import me.deshark.lms.server.user.User;
import me.deshark.lms.server.user.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userMapper.getUserByUsername(username);

        LoginUserDetails loginUserDetails = new LoginUserDetails();

        loginUserDetails.setUser(user);

        return loginUserDetails;
    }
}
