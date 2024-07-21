package com.io.hhplus.concert.interfaces.common.exception;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.*;
import com.io.hhplus.concert.common.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<Void> handleCustomException(CustomException ex) {
        log.warn("CustomException : {}", ex.getMessage());
        return CommonResponse.failure(ex.getResponseMessage(), ex.getMessage());
    }

    @ExceptionHandler(InsufficientResourcesCustomException.class)
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<Void> handleInsufficientResourcesException(InsufficientResourcesCustomException ex) {
        log.warn("InsufficientResourcesCustomException : {}", ex.getMessage());
        return CommonResponse.failure(ex.getResponseMessage(), ex.getMessage());
    }

    @ExceptionHandler(TimeOutCustomException.class)
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<Void> handleTimeOutException(TimeOutCustomException ex) {
        log.warn("TimeOutCustomException : {}", ex.getMessage());
        return CommonResponse.failure(ex.getResponseMessage(), ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<Void> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("IllegalArgumentException : {}", ex.getMessage());
        return CommonResponse.failure(ResponseMessage.INVALID, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<Void> handleException(Exception ex) {
        log.error("Exception : {}", ex.getMessage());
        return CommonResponse.failure(ResponseMessage.UNKNOWN, ex.getMessage());
    }
}
