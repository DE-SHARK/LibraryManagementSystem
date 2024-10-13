package me.deshark.lms.server;

import me.deshark.lms.server.enums.ExceptionEnum;
import me.deshark.lms.server.user.ResultResponse;
import me.deshark.lms.server.user.exceptions.UserExitedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(UserExitedException.class)
    public <T> ResultResponse<T> userExitedException() {
        return ResultResponse.error(
                ExceptionEnum.USERNAME_ALREADY_EXIST.getCode(),
                ExceptionEnum.USERNAME_ALREADY_EXIST.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("内部服务器错误：" + e.getMessage());
    }

}
