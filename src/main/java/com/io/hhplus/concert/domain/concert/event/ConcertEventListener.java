package com.io.hhplus.concert.domain.concert.event;

import com.io.hhplus.concert.domain.concert.ConcertCommand;
import com.io.hhplus.concert.domain.concert.ConcertService;
import com.io.hhplus.concert.domain.payment.event.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConcertEventListener {

    private final ConcertService concertService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void paidSuccessHandler(PaymentEvent.PaidSuccess event) {
        log.info("결제 완료 - 예약을 확정합니다.");
        ConcertCommand.ConfirmReservationCommand command = ConcertCommand.ConfirmReservationCommand.builder()
                .reservationId(event.getReservationId())
                .customerId(event.getCustomerId())
                .build();
        concertService.confirmReservation(command);
    }
}
