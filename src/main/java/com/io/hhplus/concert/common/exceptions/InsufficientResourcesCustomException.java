package com.io.hhplus.concert.common.exceptions;

import com.io.hhplus.concert.common.enums.ResponseMessage;

public class InsufficientResourcesCustomException extends AbstractBusinessLogicException {

    public InsufficientResourcesCustomException(ResponseMessage responseMessage) {
        super(responseMessage);
    }

    public InsufficientResourcesCustomException(ResponseMessage responseMessage, String message) {
        super(responseMessage, message);
    }
}
