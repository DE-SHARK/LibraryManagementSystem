package me.deshark.lms.server.enums;

import lombok.Getter;

@Getter
public enum ExceptionEnum {

    USERNAME_ALREADY_EXIST(101, "用户名已存在");

    private final int code;
    private final String message;

    ExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
