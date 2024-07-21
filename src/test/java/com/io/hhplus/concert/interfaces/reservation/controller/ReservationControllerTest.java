package com.io.hhplus.concert.interfaces.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.hhplus.concert.application.reservation.dto.*;
import com.io.hhplus.concert.application.reservation.facade.ReservationFacade;
import com.io.hhplus.concert.common.enums.PayMethod;
import com.io.hhplus.concert.common.enums.ReceiveMethod;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.interfaces.reservation.dto.request.PaymentRequest;
import com.io.hhplus.concert.interfaces.reservation.dto.request.ReservationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationFacade reservationFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void reserve() throws Exception {
        // given
        ReservationResultInfoWithTickets response = ReservationResultInfoWithTickets.builder().build();
        given(reservationFacade.requestReservation(anyLong(), any(ReserverInfo.class), any(TicketInfo.class))).willReturn(response);

        //when - then
        ReservationRequest request = ReservationRequest.builder()
                .customerId(1L)
                .reserverInfo(ReservationRequest.ReserverInfo.builder()
                        .reserverName("예매자")
                        .receiveMethod(ReceiveMethod.ONLINE)
                        .receiverName("수령인")
                        .build())
                .ticketInfo(ReservationRequest.TicketInfo.builder()
                        .concertId(1L)
                        .performanceId(1L)
                        .performancePrice(BigDecimal.valueOf(10000))
                        .performedAt(DateUtils.createTemporalDateByIntParameters(2024,9,22,11,0,0))
                        .seatIds(List.of(1L,2L,3L))
                        .build())
                .build();
        mockMvc.perform(post("/reservation/reserve")
                        .header("Authorization", "Bearer " + 1)
                        .header("customerId", 1)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void pay() throws Exception {
        // given
        PaymentResultInfoWithReservationAndTickets response = PaymentResultInfoWithReservationAndTickets.builder().build();
        given(reservationFacade.requestPayment(anyLong(), anyLong(), any())).willReturn(response);

        //when - then
        PaymentRequest request = PaymentRequest.builder()
                .customerId(1L)
                .reservationId(1L)
                .payInfos(
                        List.of(PaymentRequest.PayInfo.builder()
                                        .payAmount(BigDecimal.valueOf(10000))
                                        .payMethod(PayMethod.POINT)
                                .build())
                )
                .build();
        mockMvc.perform(post("/reservation/pay")
                        .header("Authorization", "Bearer " + 1)
                        .header("customerId", 1)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }
}