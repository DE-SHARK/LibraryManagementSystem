package me.deshark.lms.server.enums;

import lombok.Getter;

@Getter
public enum ResponseStatus {
    SUCCESS(200, "请求成功"),
    BAD_REQUEST(400, "请求错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "未找到"),
    SERVER_ERROR(500, "服务器错误");

    private final int code;
    private final String message;

    ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
