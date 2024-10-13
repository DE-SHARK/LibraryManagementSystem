package me.deshark.lms.server;

import me.deshark.lms.server.enums.ExceptionEnum;
import me.deshark.lms.server.user.ResultResponse;
import me.deshark.lms.server.user.UserExitedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(UserExitedException.class)
    public <T> ResultResponse<T> userExitedException() {
        return ResultResponse.error(
                ExceptionEnum.USERNAME_ALREADY_EXIST.getCode(),
                ExceptionEnum.USERNAME_ALREADY_EXIST.getMessage());
    }
}
