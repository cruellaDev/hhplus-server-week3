package com.io.hhplus.concert.interfaces.waiting.scheduler;

import com.io.hhplus.concert.application.waiting.facade.WaitingFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingScheduler {

    private final WaitingFacade waitingFacade;

    @Scheduled(cron = "0 0 0 * * *") // 매 0시마다 실행
    public void deleteAllExpireWaitingQueue() {
        waitingFacade.removeAllExpiredToken();
    }
}
