package com.io.hhplus.concert.infrastructure.common.client;

import com.io.hhplus.concert.domain.common.client.DataPlatformClient;
import com.io.hhplus.concert.domain.payment.event.PaymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataPlatformMockApiClientImpl implements DataPlatformClient {

    @Override
    public boolean sync(String message, PaymentEvent.PaidSuccess event) {
        try {
            Thread.sleep(6000L);
            log.info("데이터 플랫폼 동기화 성공: {}", message);
            return true;
        } catch (InterruptedException e) {
            throw new RuntimeException("데이터 플랫폼 동기화 실패", e);
        }
    }
}
