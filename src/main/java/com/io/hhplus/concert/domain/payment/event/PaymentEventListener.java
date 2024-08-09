package com.io.hhplus.concert.domain.payment.event;

import com.io.hhplus.concert.domain.common.client.DataPlatformClient;
import com.io.hhplus.concert.domain.common.client.PushClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {
    private final PushClient pushClient;
    private final DataPlatformClient dataPlatformClient;

    private final String PAID_SUCCESS = "결제완료";

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void paidSuccessHandler(PaymentEvent.PaidSuccess event) {
        log.info("{} 정보를 전송합니다.", PAID_SUCCESS);
        try {
            log.info("[Event][KakaoTalk] 카카오 알림톡을 전송합니다.");
            pushClient.sendKakaoTalkNotification(PAID_SUCCESS, event);
            log.info("[Event][KakaoTalk] 카카오 알림톡 전송이 완료되었습니다.");


            log.info("[Event][DataPlatform] 동기화를 진행합니다.");
            dataPlatformClient.sync(PAID_SUCCESS, event);
            log.info("[Event][DataPlatform] 동기화가 완료되었습니다.");
        } catch (Exception e) {
            log.error("[Event][Error]", e);
        }
    }
}
