package com.io.hhplus.concert.common.exceptions;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class IllegalStateCustomException extends RuntimeException {
    private final ResponseMessage responseMessage;

    public IllegalStateCustomException(ResponseMessage responseMessage) {
        super();
        this.responseMessage = responseMessage;
    }

    public IllegalStateCustomException(String message, Throwable cause, ResponseMessage responseMessage) {
        super(message, cause);
        this.responseMessage = responseMessage;
    }

    public IllegalStateCustomException(String message, ResponseMessage responseMessage) {
        super(message);
        this.responseMessage = responseMessage;
    }

    public IllegalStateCustomException(Throwable cause, ResponseMessage responseMessage) {
        super(cause);
        this.responseMessage = responseMessage;
    }
}
