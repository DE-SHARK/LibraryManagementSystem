package me.deshark.lms.server.user.service.impl;


import jakarta.annotation.Resource;
import me.deshark.lms.server.user.LoginUserDetails;
import me.deshark.lms.server.user.User;
import me.deshark.lms.server.user.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("正在查找用户: {}", username);
        User user = userMapper.getUserByUsername(username);

        if (user == null) {
            log.debug("未找到用户: {}", username);
            throw new UsernameNotFoundException(username);
        }

        log.debug("找到用户: {}", user);
        return new LoginUserDetails(user);
    }
}
