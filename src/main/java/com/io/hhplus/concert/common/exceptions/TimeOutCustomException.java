package com.io.hhplus.concert.common.exceptions;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
public class TimeOutCustomException extends AbstractBusinessLogicException {

    public TimeOutCustomException(ResponseMessage responseMessage) {
        super(responseMessage);
    }

    public TimeOutCustomException(ResponseMessage responseMessage, String message) {
        super(responseMessage, message);
    }
}
