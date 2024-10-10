package me.deshark.lms.server.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private int code;

    private String message;

    private T data;

    public static <E> Result<E> success(E data) {
        return new Result<>(0,"操作成功", data);
    }

    public static <E> Result<E> error(E data) {
        return new Result<>(1,"操作失败", data);
    }

    public static Result success() {
        return new Result(0,"操作成功",null);
    }

    public static Result error() {
        return new Result(1,"操作失败",null);
    }

}
