package com.io.hhplus.concert.common.exceptions;

import com.io.hhplus.concert.common.enums.ResponseMessage;

public abstract class AbstractBusinessLogicException extends AbstractCustomException {

    public AbstractBusinessLogicException(ResponseMessage responseMessage) {
        super(responseMessage);
    }

    public AbstractBusinessLogicException(ResponseMessage responseMessage, String message) {
        super(responseMessage, message);
    }
}
