package com.io.hhplus.concert.common.exceptions;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import lombok.Getter;

@Getter
public abstract class AbstractCustomException extends RuntimeException {

    private final ResponseMessage responseMessage;
    private final String messageDetail;

    public AbstractCustomException(ResponseMessage responseMessage) {
        super(responseMessage.getMessageDetail());
        this.responseMessage = responseMessage;
        this.messageDetail = responseMessage.getMessageDetail();
    }

    public AbstractCustomException(ResponseMessage responseMessage, String message) {
        super(message);
        this.responseMessage = responseMessage;
        this.messageDetail = message;
    }

}
