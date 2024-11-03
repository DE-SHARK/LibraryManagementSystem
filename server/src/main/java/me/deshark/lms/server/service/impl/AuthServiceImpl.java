package me.deshark.lms.server.service.impl;

import me.deshark.lms.server.model.dto.AuthRequest;
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
import me.deshark.lms.server.utils.JwtUtil;
import org.springframework.security.core.AuthenticationException;

@Service
public class AuthServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public ResultResponse<String> registerUser(AuthRequest authRequest) {

        if (getUserByUsername(authRequest.getUsername()) != null) {
            throw new UserExitedException("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(authRequest.getPassword());
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(hashedPassword);
        user.setRole("BORROWER");
        userMapper.insertUser(user);
        return ResultResponse.success();

    }

    @Override
    public ResultResponse<String> loginUser(AuthRequest authRequest) {
        try {
            // 验证用户名密码
            Authentication authenticationRequest =
                    UsernamePasswordAuthenticationToken.unauthenticated(authRequest.getUsername(), authRequest.getPassword());
            Authentication authenticationResponse =
                    authenticationManager.authenticate(authenticationRequest);
                    
            // 生成JWT token
            String token = jwtUtil.generateToken(authRequest.getUsername());
            
            return ResultResponse.success(token);
        } catch (AuthenticationException e) {
            return ResultResponse.error(401, "用户名或密码错误");
        }
    }
}
