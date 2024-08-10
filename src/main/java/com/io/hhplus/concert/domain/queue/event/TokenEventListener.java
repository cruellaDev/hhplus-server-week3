package com.io.hhplus.concert.domain.queue.event;

import com.io.hhplus.concert.domain.payment.event.PaymentEvent;
import com.io.hhplus.concert.domain.queue.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenEventListener {
    private final TokenService tokenService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void paidSuccessHandler(PaymentEvent.PaidSuccess event) {
        log.info("결제 완료 - 토큰을 만료합니다.");
        tokenService.expireIndividualToken(event.getToken());
    }
}
