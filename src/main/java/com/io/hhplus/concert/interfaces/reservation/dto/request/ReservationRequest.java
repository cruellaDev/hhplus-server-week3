package com.io.hhplus.concert.interfaces.reservation.dto.request;

import com.io.hhplus.concert.common.enums.ReceiveMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ReservationRequest {

    @NotNull
    private final Long customerId;

    @NotNull
    private final ReserverInfo reserverInfo;

    @NotNull
    private final TicketInfo ticketInfo;

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ReserverInfo {
        @NotBlank
        private String reserverName;

        @NotNull
        private ReceiveMethod receiveMethod;

        @NotBlank
        private String receiverName;

        private String receiverPostcode;
        private String receiverBaseAddress;
        private String receiverDetailAddress;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class TicketInfo {
        @NotNull
        private final Long concertId;

        @NotNull
        private final Long performanceId;

        @NotNull
        @Min(0)
        private final BigDecimal performancePrice;

        @NotNull
        private final Date performedAt;

        @NotEmpty
        private final List<Long> seatIds;
    }
}
