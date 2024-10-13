package me.deshark.lms.server;

import me.deshark.lms.server.enums.ExceptionEnum;
import me.deshark.lms.server.user.ResultResponse;
import me.deshark.lms.server.user.exceptions.UserExitedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public <T> ResultResponse<T> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = sb.toString();
        return ResultResponse.error(1, msg);
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
