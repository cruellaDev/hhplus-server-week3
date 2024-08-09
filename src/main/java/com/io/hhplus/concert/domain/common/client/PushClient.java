package com.io.hhplus.concert.domain.common.client;

import com.io.hhplus.concert.domain.payment.event.PaymentEvent;

public interface PushClient {
    boolean sendKakaoTalkNotification(String message, PaymentEvent.PaidSuccess event);
}
