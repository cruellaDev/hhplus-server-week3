package com.io.hhplus.concert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.hhplus.concert.interfaces.concert.controller.ConcertController;
import com.io.hhplus.concert.interfaces.customer.controller.CustomerController;
import com.io.hhplus.concert.interfaces.customer.dto.request.CustomerPointRequest;
import com.io.hhplus.concert.interfaces.reservation.controller.ReservationController;
import com.io.hhplus.concert.interfaces.waiting.controller.WaitingController;
import org.junit.jupiter.api.Disabled;
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
    @Disabled
    public void getCustomerPointTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        mockMvc.perform(post("/customer/point")
                        .header("customerId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                ;
    }

    @Test
    @Disabled
    public void chargeCustomerPointTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        CustomerPointRequest requestBody = new CustomerPointRequest(1L, BigDecimal.valueOf(1000));
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
