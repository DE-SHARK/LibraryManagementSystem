package me.deshark.lms.server.user.service.impl;

import me.deshark.lms.server.user.User;
import me.deshark.lms.server.user.UserMapper;
import me.deshark.lms.server.exception.UserExitedException;
import me.deshark.lms.server.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public boolean registerUser(User user) {

        if (getUserByUsername(user.getUsername()) != null) {
            throw new UserExitedException("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return userMapper.insertUser(user) > 0;

    }
}