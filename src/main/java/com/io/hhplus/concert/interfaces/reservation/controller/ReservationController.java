package com.io.hhplus.concert.interfaces.reservation.controller;

import com.io.hhplus.concert.application.reservation.dto.PaymentRequest;
import com.io.hhplus.concert.application.reservation.dto.PaymentResponse;
import com.io.hhplus.concert.application.reservation.dto.ReservationRequest;
import com.io.hhplus.concert.application.reservation.dto.ReservationResponse;
import com.io.hhplus.concert.application.reservation.facade.ReservationFacade;
import com.io.hhplus.concert.interfaces.common.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("reservation")
public class ReservationController {

    private final ReservationFacade reservationFacade;

    /**
     * 좌석 예약 요청
     * @param requestBody 요청 정보
     * @return 응답 정보
     */
    @PostMapping("/reserve")
    public CommonResponse<ReservationResponse> reserve(@RequestBody ReservationRequest requestBody) {
        return CommonResponse.success(reservationFacade.requestReservation(requestBody));
    }

    /**
     * 결제 요청
     * @param requestBody 요청 정보
     * @return 응답 정보
     */
    @PostMapping("/pay")
    public CommonResponse<PaymentResponse> pay(@RequestBody PaymentRequest requestBody) {
        return CommonResponse.success(reservationFacade.requestPayment(requestBody));
    }

}
