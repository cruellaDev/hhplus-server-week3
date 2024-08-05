package com.io.hhplus.concert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.hhplus.concert.interfaces.concert.controller.ConcertController;
import com.io.hhplus.concert.interfaces.customer.controller.CustomerController;
import com.io.hhplus.concert.interfaces.payment.controller.PaymentController;
import com.io.hhplus.concert.interfaces.token.controller.TokenController;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Disabled
public class MockApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerController customerController;

    @MockBean
    private ConcertController concertController;

    @MockBean
    private PaymentController reservationController;

    @MockBean
    private TokenController waitingController;

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

//    @Test
//    @Disabled
//    public void chargeCustomerPointTest() throws Exception {
//        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
//
//        CustomerPointRequest requestBody = new CustomerPointRequest(1L, BigDecimal.valueOf(1000));
//        String content = objectMapper.writeValueAsString(requestBody);
//
//        mockMvc.perform(post("/customer/point/charge")
//                        .content(content)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print())
//        ;
//    }

}
