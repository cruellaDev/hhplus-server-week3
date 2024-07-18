package com.io.hhplus.concert.common.exceptions;

import com.io.hhplus.concert.common.enums.ResponseMessage;

public class CustomException extends AbstractBusinessLogicException{
    public CustomException(ResponseMessage responseMessage) {
        super(responseMessage);
    }

    public CustomException(ResponseMessage responseMessage, String message) {
        super(responseMessage, message);
    }
}
