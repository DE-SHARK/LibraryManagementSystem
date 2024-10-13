package me.deshark.lms.server.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.deshark.lms.server.enums.ResponseStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResultResponse<T> {

    private int code;

    private String message;

    private T data;

    // 成功
    public static <T> ResultResponse<T> success() {
        return success(null);
    }

    // 成功
    public static <T> ResultResponse<T> success(T data) {
        ResultResponse<T> result = new ResultResponse<>();
        result.setCode(ResponseStatus.SUCCESS.getCode());
        result.setMessage(ResponseStatus.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    // 失败
    public static <T> ResultResponse<T> error(int code, String message) {
        ResultResponse<T> result = new ResultResponse<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

}
