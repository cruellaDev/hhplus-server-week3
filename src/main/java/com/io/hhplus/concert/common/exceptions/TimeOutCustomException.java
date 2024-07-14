package com.io.hhplus.concert.common.exceptions;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
public class TimeOutCustomException extends RuntimeException {

    private final ResponseMessage responseMessage;

    public TimeOutCustomException(ResponseMessage responseMessage) {
        super();
        this.responseMessage = responseMessage;
    }

    public TimeOutCustomException(String message, Throwable cause, ResponseMessage responseMessage) {
        super(message, cause);
        this.responseMessage = responseMessage;
    }

    public TimeOutCustomException(String message, ResponseMessage responseMessage) {
        super(message);
        this.responseMessage = responseMessage;
    }

    public TimeOutCustomException(Throwable cause, ResponseMessage responseMessage) {
        super(cause);
        this.responseMessage = responseMessage;
    }
}
