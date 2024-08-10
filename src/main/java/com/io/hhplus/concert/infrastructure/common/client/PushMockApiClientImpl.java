package com.io.hhplus.concert.infrastructure.common.client;

import com.io.hhplus.concert.domain.common.client.PushClient;
import com.io.hhplus.concert.domain.payment.event.PaymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PushMockApiClientImpl implements PushClient {
    @Override
    public boolean sendKakaoTalkNotification(String message, PaymentEvent.PaidSuccess event) {
        try {
            Thread.sleep(6000L);
            log.info("카카오 알림톡 전송 성공: {}", message);
            return true;
        } catch (InterruptedException e) {
            throw new RuntimeException("카카오 알림톡 전송 실패", e);
        }
    }
}
