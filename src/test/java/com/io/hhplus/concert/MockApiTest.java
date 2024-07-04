package com.io.hhplus.concert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.hhplus.concert.presentation.concert.controller.ConcertController;
import com.io.hhplus.concert.presentation.customer.controller.CustomerController;
import com.io.hhplus.concert.presentation.customer.dto.request.PostBalanceRequestBody;
import com.io.hhplus.concert.presentation.customer.dto.request.PostChargeRequestBody;
import com.io.hhplus.concert.presentation.reservation.controller.ReservationController;
import com.io.hhplus.concert.presentation.waiting.controller.WaitingController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class MockApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerController customerController;

    @MockBean
    private ConcertController concertController;

    @MockBean
    private ReservationController reservationController;

    @MockBean
    private WaitingController waitingController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void balanceCustomerPointTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        PostBalanceRequestBody requestBody = new PostBalanceRequestBody(1L);
        String content = objectMapper.writeValueAsString(requestBody);

        mockMvc.perform(post("/customer/point/balance")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                ;
    }

    @Test
    public void chargeCustomerPointTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        PostChargeRequestBody requestBody = new PostChargeRequestBody(1L, BigDecimal.valueOf(1000));
        String content = objectMapper.writeValueAsString(requestBody);

        mockMvc.perform(post("/customer/point/charge")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }

}
