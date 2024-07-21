package com.io.hhplus.concert.interfaces.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.hhplus.concert.application.reservation.dto.*;
import com.io.hhplus.concert.interfaces.reservation.dto.request.PaymentRequest;
import com.io.hhplus.concert.interfaces.reservation.dto.request.ReservationRequest;
import com.io.hhplus.concert.application.reservation.facade.ReservationFacade;
import com.io.hhplus.concert.common.dto.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("reservation")
public class ReservationController {

    private final ReservationFacade reservationFacade;
    private final ObjectMapper objectMapper;

    /**
     * 좌석 예약 요청
     * @param requestBody 요청 정보
     * @return 응답 정보
     */
    @PostMapping("/reserve")
    public CommonResponse<ReservationResultInfoWithTickets> reserve(@RequestBody @Valid ReservationRequest requestBody) {
        ReserverInfo reserverInfo = objectMapper.convertValue(requestBody.getReserverInfo(), ReserverInfo.class);
        TicketInfo ticketInfo = objectMapper.convertValue(requestBody.getTicketInfo(), TicketInfo.class);
        return CommonResponse.success(reservationFacade.requestReservation(requestBody.getCustomerId(), reserverInfo, ticketInfo));
    }

    /**
     * 결제 요청
     * @param requestBody 요청 정보
     * @return 응답 정보
     */
    @PostMapping("/pay")
    public CommonResponse<PaymentResultInfoWithReservationAndTickets> pay(@RequestBody @Valid PaymentRequest requestBody) {
        List<PaymentInfo> paymentInfos = requestBody.getPayInfos().stream().map(payInfo -> objectMapper.convertValue(payInfo, PaymentInfo.class)).toList();
        return CommonResponse.success(reservationFacade.requestPayment(requestBody.getCustomerId(), requestBody.getReservationId(), paymentInfos));
    }

}
