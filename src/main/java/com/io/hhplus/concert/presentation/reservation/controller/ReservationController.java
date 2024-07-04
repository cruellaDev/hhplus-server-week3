package com.io.hhplus.concert.presentation.reservation.controller;

import com.io.hhplus.concert.presentation.common.dto.ErrorResponseBody;
import com.io.hhplus.concert.presentation.reservation.dto.PayDto;
import com.io.hhplus.concert.presentation.reservation.dto.request.PostPayRequestBody;
import com.io.hhplus.concert.presentation.reservation.dto.request.PostReserveRequestBody;
import com.io.hhplus.concert.presentation.reservation.dto.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RequestMapping("reservation")
@RestController
public class ReservationController {

    /**
     * 좌석 예약 요청
     * @param requestBody 요청 정보
     * @return 응답 정보
     */
    @PostMapping("/reserve")
    public ResponseEntity<Object> reserve(@RequestBody PostReserveRequestBody requestBody) {
        if (requestBody.getConcertId().equals(9999L)) {
            return ResponseEntity.status(500).body(new ErrorResponseBody("ALREADY_TAKEN", "이미 선점된 좌석입니다."));
        }
        PostReservationResponseBody reservation = new PostReservationResponseBody(1L, 1L, "이지원", "예약요청");
        List<PostTicketResponseBody> tickets = List.of(new PostTicketResponseBody(requestBody.getConcertId(), "허재 코치님 팬미팅", 1L, new Date(), BigDecimal.TEN, "결제대기", 1L, "07"));
        return ResponseEntity.ok().body(new PostReserveResponseBody(reservation, tickets));
    }

    /**
     * 결제 요청
     * @param requestBody 요청 정보
     * @return 응답 정보
     */
    @RequestMapping("/pay")
    public ResponseEntity<Object> pay(@RequestBody PostPayRequestBody requestBody) {
        if (requestBody.getCustomerId().equals(9999L)) {
            return ResponseEntity.status(500).body(new ErrorResponseBody("OUT_OF_BUDGET", "포인트 잔액이 부족합니다."));
        }
        List<PostPayedTicketsResponseBody> tickets = List.of(new PostPayedTicketsResponseBody(
                "이지원",
                1L,
                "허재 코치님 팬미팅",
                1L,
                new Date(),
                BigDecimal.TEN,
                "결제완료",
                1L,
                "07",
                new Date()
        ));
        List<PayDto> pay = List.of(new PayDto("포인트", BigDecimal.TEN), new PayDto("신용카드", BigDecimal.ZERO));
        PostPayResponseBody responseBody = new PostPayResponseBody(requestBody.getCustomerId(), tickets, pay);
        return ResponseEntity.ok().body(responseBody);
    }

}
