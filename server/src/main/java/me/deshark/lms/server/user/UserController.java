package me.deshark.lms.server.user;

import me.deshark.lms.server.enums.ResponseStatus;
import me.deshark.lms.server.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

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
}
