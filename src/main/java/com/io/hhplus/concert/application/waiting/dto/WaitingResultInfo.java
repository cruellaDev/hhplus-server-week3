package com.io.hhplus.concert.application.waiting.dto;

import com.io.hhplus.concert.common.enums.WaitingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WaitingResultInfo {
    private Long customerId;
    private Long numberOfWaiting;
    private Long waitingNumber;
    private WaitingStatus waitingStatus;
    private Long remainingTime;
}
