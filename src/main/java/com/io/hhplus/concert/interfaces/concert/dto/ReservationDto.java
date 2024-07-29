package com.io.hhplus.concert.interfaces.concert.dto;

import com.io.hhplus.concert.application.concert.dto.HeldSeatServiceResponse;
import com.io.hhplus.concert.application.concert.dto.HoldSeatServiceRequest;
import com.io.hhplus.concert.common.enums.SeatStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationDto {

    public static class HoldSeatsRequest {
        private Long customerId;
        private Long concertId;
        private Long performanceId;
        private Long areaId;
        private List<SeatRequest> seats;

        @Builder
        @Getter
        public static class SeatRequest {
            private String seatNumber;
        }

        public HoldSeatServiceRequest toServiceRequest() {
            return HoldSeatServiceRequest.builder()
                    .customerId(this.customerId)
                    .concertId(this.concertId)
                    .performanceId(this.performanceId)
                    .areaId(this.areaId)
                    .seats(this.seats
                            .stream()
                            .map(seat -> HoldSeatServiceRequest.SeatRequest.builder().seatNumber(seat.seatNumber).build())
                            .collect(Collectors.toList())
                    )
                    .build();
        }
    }

    @Builder
    public static class HoldSeatsResponse {

        private List<HeldSeatResponse> heldSeats;

        @Builder
        public static class HeldSeatResponse {
            private Long reservationId;
            private Long concertId;
            private Long performanceId;
            private Long areaId;
            private String seatNumber;
            private SeatStatus seatStatus;
        }

        public static ReservationDto.HoldSeatsResponse from(List<HeldSeatServiceResponse> seats) {
            List<HoldSeatsResponse.HeldSeatResponse> heldSeats = seats
                    .stream()
                    .map(seat -> HeldSeatResponse.builder()
                            .reservationId(seat.reservationId())
                            .concertId(seat.concertId())
                            .performanceId(seat.performanceId())
                            .areaId(seat.areaId())
                            .seatNumber(seat.seatNumber())
                            .seatStatus(seat.seatStatus())
                            .build())
                    .toList();
            return HoldSeatsResponse.builder()
                    .heldSeats(heldSeats)
                    .build();
        }
    }

}
