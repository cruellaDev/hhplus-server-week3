package com.io.hhplus.concert.application.concert.dto;

import com.io.hhplus.concert.common.enums.ReceiveMethod;
import com.io.hhplus.concert.domain.concert.model.Reservation;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConfirmReservationServiceRequest {
    private Long customerId;
    private Long reservationId;
    private String reserverName;

    public Reservation.ReceiveInfo toReceiveInfoDomain() {
        return Reservation.ReceiveInfo.builder()
                .reserverName(this.reserverName)
                .build();
    }
}
