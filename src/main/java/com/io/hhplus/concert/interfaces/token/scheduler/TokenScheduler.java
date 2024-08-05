package com.io.hhplus.concert.interfaces.token.scheduler;

import com.io.hhplus.concert.application.queue.QueueTokenFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenScheduler {

    private final QueueTokenFacade queueTokenFacade;

    /**
     * 토큰 만료 처리 - 매 2분마다 실행
     */
    @Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
    public void expireToken() {
        queueTokenFacade.expireToken();
    }

    /**
     * 토큰 활성화 처리 - 매 3분마다 실행
     */
    @Scheduled(fixedRate = 3, timeUnit = TimeUnit.MINUTES)
    public void activateToken() {
        queueTokenFacade.activateToken();
    }
}
