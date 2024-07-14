package com.io.hhplus.concert.common.exceptions;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalArgumentCustomException extends RuntimeException {

    private final ResponseMessage responseMessage;

    public IllegalArgumentCustomException(ResponseMessage responseMessage) {
        super();
        this.responseMessage = responseMessage;
    }

    public IllegalArgumentCustomException(String message, Throwable cause, ResponseMessage responseMessage) {
        super(message, cause);
        this.responseMessage = responseMessage;
    }

    public IllegalArgumentCustomException(String message, ResponseMessage responseMessage) {
        super(message);
        this.responseMessage = responseMessage;
    }

    public IllegalArgumentCustomException(Throwable cause, ResponseMessage responseMessage) {
        super(cause);
        this.responseMessage = responseMessage;
    }
}
