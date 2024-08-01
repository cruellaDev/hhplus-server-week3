package com.io.hhplus.concert.domain.queue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TokenCommand {
    /**
     * 은행창구 방식 대기열 토큰 발급 및 조회
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class IssueTokenBankCounterCommand {
        private Long customerId;
    }

}
