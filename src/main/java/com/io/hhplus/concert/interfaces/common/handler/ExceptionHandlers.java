package com.io.hhplus.concert.interfaces.common.handler;

import com.io.hhplus.concert.common.exceptions.*;
import com.io.hhplus.concert.interfaces.common.dto.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(ResourceNotFoundCustomException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse<Void> handleResourceNotFoundException(ResourceNotFoundCustomException ex) {
        return CommonResponse.failure(ex.getResponseMessage(), ex.getMessage());
    }

    @ExceptionHandler(InsufficientResourcesCustomException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public CommonResponse<Void> handleInsufficientResourcesException(InsufficientResourcesCustomException ex) {
        return CommonResponse.failure(ex.getResponseMessage(), ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentCustomException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<Void> handleIllegalArgumentException(IllegalArgumentCustomException ex) {
        return CommonResponse.failure(ex.getResponseMessage(), ex.getMessage());
    }

    @ExceptionHandler(IllegalStateCustomException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<Void> handleIllegalStateException(IllegalStateCustomException ex) {
        return CommonResponse.failure(ex.getResponseMessage(), ex.getMessage());
    }
    
    @ExceptionHandler(TimeOutCustomException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public CommonResponse<Void> handleTimeOutException(TimeOutCustomException ex) {
        return CommonResponse.failure(ex.getResponseMessage(), ex.getMessage());
    }
}
