package com.xy.common.base;

import com.xy.common.constant.HttpStatus;
import com.xy.common.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 响应信息主体
 *
 * @author valarchie
 */
@Data
@AllArgsConstructor
public class BaseResponseData<T> {
    private Integer code;
    private String msg;
//    @JsonInclude
    private T data;

    public static <T> BaseResponseData<T> ok() {
        return build(null, HttpStatus.SUCCESS, "OK");
    }

    public static <T> BaseResponseData<T> ok(T data) {
        return build(data, HttpStatus.SUCCESS, "OK");
    }

    public static <T> BaseResponseData<T> fail() {
        return build(null, HttpStatus.ERROR,"ERROR");
    }

    public static <T> BaseResponseData<T> fail(T data) {
        return build(data, HttpStatus.ERROR,"ERROR");
    }

    public static <T> BaseResponseData<T> fail(ApiException exception) {
        return build(null, exception.getErrorCode().code(), exception.getMessage());
    }

    public static <T> BaseResponseData<T> fail(ApiException exception, T data) {
        return build(data, exception.getErrorCode().code(), exception.getMessage());
    }

    public static <T> BaseResponseData<T> build(T data, Integer code, String msg) {
        return new BaseResponseData<>(code, msg, data);
    }
//这里我先不用i18n了
    // 去掉直接填充错误码的方式， 这种方式不能拿到i18n的错误消息  统一通过ApiException来构造错误消息
//    public static <T> BaseResponseData<T> fail(ErrorCodeInterface code, Object... args) {
//        return build(null, code, args);
//    }

}

