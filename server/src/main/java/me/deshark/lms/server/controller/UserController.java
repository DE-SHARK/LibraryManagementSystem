package me.deshark.lms.server.controller;

import me.deshark.lms.server.constant.enums.ResponseStatus;
import me.deshark.lms.server.service.intf.IUserService;
import me.deshark.lms.server.utils.ResultResponse;
import me.deshark.lms.server.model.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @RequestMapping("/getUserById")
    public User getUserById(Long id) {
        return userService.getUserById(id);
    }

    // 注册
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Validated User user) {
        boolean result = userService.registerUser(user);
        if (result) {
            return ResponseEntity
                    .status(ResponseStatus.SUCCESS.getCode())
                    .body(ResponseStatus.SUCCESS.getMessage());
        } else {
            return ResponseEntity
                    .status(ResponseStatus.BAD_REQUEST.getCode())
                    .body(ResponseStatus.BAD_REQUEST.getMessage());
        }
    }

    // 登录
    @PostMapping("/login")
    public ResultResponse<String> login(@RequestBody @Validated User user) {
        log.debug("收到登录请求: {}", user);
        return userService.loginUser(user);
    }

}
