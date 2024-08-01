package com.io.hhplus.concert.application.queue;

import com.io.hhplus.concert.domain.queue.TokenCommand;
import com.io.hhplus.concert.domain.queue.TokenService;
import com.io.hhplus.concert.domain.queue.dto.BankCounterQueueTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueTokenFacade {

    private final TokenService tokenService;

    /**
     * 은행창구식 대기열 토큰 발급 및 조회
     * @param command 대기열 토큰 발급 및 조회 command
     * @return 대기열 정보
     */
    public BankCounterQueueTokenInfo issueToken(TokenCommand.IssueTokenBankCounterCommand command) {
        return tokenService.issueToken(command);
    }

    /**
     * 대기열 토큰 활성화 처리 (Scheduler)
     */
    public void activateToken() {
        tokenService.activateToken();
    }

    /**
     * 대기열 토큰 만료 처리 (Scheduler)
     */
    public void expireToken() {
        tokenService.expireToken();
    }
}
