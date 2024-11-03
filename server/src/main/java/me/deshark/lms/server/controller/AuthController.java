package me.deshark.lms.server.controller;

import me.deshark.lms.server.model.dto.AuthRequest;
import me.deshark.lms.server.service.intf.IUserService;
import me.deshark.lms.server.utils.ResultResponse;
import me.deshark.lms.server.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AuthController {

    @Autowired
    private IUserService authService;

    @RequestMapping("/getUserById")
    public User getUserById(Long id) {
        return authService.getUserById(id);
    }

    // 注册
    @PostMapping("/register")
    public ResultResponse<String> register(@RequestBody @Validated AuthRequest authRequest) {
        return authService.registerUser(authRequest);
    }

    // 登录
    @PostMapping("/login")
    public ResultResponse<String> login(@RequestBody @Validated AuthRequest authRequest) {
        return authService.loginUser(authRequest);
    }

}
