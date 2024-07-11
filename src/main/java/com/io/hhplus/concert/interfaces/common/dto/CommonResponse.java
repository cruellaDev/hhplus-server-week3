package com.io.hhplus.concert.interfaces.common.dto;

import com.io.hhplus.concert.common.enums.ResponseMessage;

public record CommonResponse<T> (
        ResponseMessage responseStatus,
        String message,
        T data)
{
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(ResponseMessage.OK, null, data);
    }

    public static <T> CommonResponse<T> success(T data, String message) {
        return new CommonResponse<>(ResponseMessage.OK, message, data);
    }

    public static <T> CommonResponse<T> success() {
        return new CommonResponse<>(ResponseMessage.OK,null, null);
    }

    public static <T> CommonResponse<T> success(String message) {
        return new CommonResponse<>(ResponseMessage.OK, message, null);
    }

    public static <T> CommonResponse<T> failure(String message) {
        return new CommonResponse<>(ResponseMessage.FAIL, message, null);
    }

    public static <T> CommonResponse<T> failure(ResponseMessage status, String message) {
        return new CommonResponse<>(status, message, null);
    }
}
