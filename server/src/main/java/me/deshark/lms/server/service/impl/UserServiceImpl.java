package me.deshark.lms.server.service.impl;

import me.deshark.lms.server.utils.ResultResponse;
import me.deshark.lms.server.model.entity.User;
import me.deshark.lms.server.mapper.UserMapper;
import me.deshark.lms.server.exception.UserExitedException;
import me.deshark.lms.server.service.intf.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

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

    @Override
    public ResultResponse<String> loginUser(User user) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(user.getUsername(), user.getPassword());
        Authentication authenticationResponse =
                authenticationManager.authenticate(authenticationRequest);
        return ResultResponse.success(authenticationResponse.getPrincipal().toString());
    }
}
