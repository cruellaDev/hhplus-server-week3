package com.io.hhplus.concert.interfaces.reservation.scheduler;

import com.io.hhplus.concert.application.reservation.facade.ReservationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationFacade reservationFacade;

    @Scheduled(fixedRate = 30000) // 30초마다 스케줄러 실행
    public void removeOccupiedSeat() {
        reservationFacade.removeWaitingReservation(); // 배정된 좌석 중 결제하지 않고 대기중인 예약, 티켓, 좌석 제거(최대 5분)
    }
}
