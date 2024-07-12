package com.io.hhplus.concert.application.reservation.dto;

import com.io.hhplus.concert.common.enums.PayMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class PaymentRequest {

    private final Long customerId;
    private final Long reservationId;
    private final List<PayInfo> payInfos;

    @Getter
    @AllArgsConstructor
    public static class PayInfo {
        private final PayMethod payMethod;
        private BigDecimal payAmount;
    }
}
