package com.io.hhplus.concert.common.exceptions;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundCustomException extends RuntimeException {

    private final ResponseMessage responseMessage;

    public ResourceNotFoundCustomException(ResponseMessage responseMessage) {
        super();
        this.responseMessage = responseMessage;
    }

    public ResourceNotFoundCustomException(String message, Throwable cause, ResponseMessage responseMessage) {
        super(message, cause);
        this.responseMessage = responseMessage;
    }

    public ResourceNotFoundCustomException(String message, ResponseMessage responseMessage) {
        super(message);
        this.responseMessage = responseMessage;
    }

    public ResourceNotFoundCustomException(Throwable cause, ResponseMessage responseMessage) {
        super(cause);
        this.responseMessage = responseMessage;
    }
}
