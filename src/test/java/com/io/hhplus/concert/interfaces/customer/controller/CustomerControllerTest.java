package com.io.hhplus.concert.interfaces.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.hhplus.concert.application.customer.dto.CustomerInfo;
import com.io.hhplus.concert.application.customer.dto.CustomerInfoWithCustomerPointHistory;
import com.io.hhplus.concert.application.customer.facade.CustomerFacade;
import com.io.hhplus.concert.interfaces.customer.dto.request.CustomerPointRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerFacade customerFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCustomerPoint() throws Exception {
        //given
        CustomerInfo response = CustomerInfo.builder().build();
        given(customerFacade.getCustomerPoint(anyLong())).willReturn(response);

        //when - then
        mockMvc.perform(get("/customer/point")
                        .header("Authorization", "Bearer " + 1)
                        .header("customerId", 1)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void chargeCustomerPoint() throws Exception {
        //given
        CustomerInfoWithCustomerPointHistory response = CustomerInfoWithCustomerPointHistory.builder().build();
        given(customerFacade.chargeCustomerPoint(anyLong(), any())).willReturn(response);

        //when - then
        CustomerPointRequest request = CustomerPointRequest.builder()
                .customerId(1L)
                .pointAmount(BigDecimal.valueOf(10000))
                .build();
        mockMvc.perform(post("/customer/point/charge")
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