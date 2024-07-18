package com.io.hhplus.concert.interfaces.reservation.dto.request;

import com.io.hhplus.concert.common.enums.PayMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PaymentRequest {

    @NotNull
    private final Long customerId;

    @NotNull
    private final Long reservationId;

    @NotEmpty
    private final List<PayInfo> payInfos;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PayInfo {
        @NotNull
        private final PayMethod payMethod;

        @Min(0)
        private BigDecimal payAmount;
    }
}
