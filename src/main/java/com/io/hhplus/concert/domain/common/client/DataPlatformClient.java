package com.io.hhplus.concert.domain.common.client;

import com.io.hhplus.concert.domain.payment.event.PaymentEvent;

public interface DataPlatformClient {
    boolean success(String message, PaymentEvent.PaidSuccess event);
}
