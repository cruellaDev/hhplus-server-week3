package com.io.hhplus.concert.interfaces.token.dto;

import com.io.hhplus.concert.common.enums.QueueStatus;
import com.io.hhplus.concert.domain.queue.TokenCommand;
import com.io.hhplus.concert.domain.queue.dto.BankCounterQueueTokenInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class QueueTokenDto {

    @Data
    public static class BankCounterIssueTokenRequest {
        private Long customerId;

        public TokenCommand.IssueTokenBankCounterCommand toCommand() {
            return TokenCommand.IssueTokenBankCounterCommand.builder()
                    .customerId(this.customerId)
                    .build();
        }
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BankCounterIssueTokenResponse {
        private Long customerId;
        private QueueStatus queueStatus;
        private Long waitingCountAhead;
        private Long waitingCountBehind;

        public static QueueTokenDto.BankCounterIssueTokenResponse from(BankCounterQueueTokenInfo bankCounterQueueTokenInfo) {
            return QueueTokenDto.BankCounterIssueTokenResponse.builder()
                    .customerId(bankCounterQueueTokenInfo.customerId())
                    .queueStatus(bankCounterQueueTokenInfo.queueStatus())
                    .waitingCountAhead(bankCounterQueueTokenInfo.waitingCountAhead())
                    .waitingCountBehind(bankCounterQueueTokenInfo.waitingCountBehind())
                    .build();
        }
    }



}
