package com.io.hhplus.concert.interfaces.waiting.controller;

import com.io.hhplus.concert.application.waiting.dto.WaitingResultInfo;
import com.io.hhplus.concert.application.waiting.facade.WaitingFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WaitingController.class)
class WaitingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WaitingFacade waitingFacade;

    /**
     * 대기열 진입
     */
    @Test
    void enter() throws Exception {
        //given
        WaitingResultInfo response = WaitingResultInfo.builder().build();
        given(waitingFacade.publishWaitingToken(anyLong())).willReturn(response);

        //when - then
        mockMvc.perform(post("/waiting/enter")
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
    void check() throws Exception {
        //given
        WaitingResultInfo response = WaitingResultInfo.builder().build();
        given(waitingFacade.getWaitingToken(anyLong())).willReturn(response);

        //when - then
        mockMvc.perform(get("/waiting/check")
                        .header("Authorization", "Bearer " + 1)
                        .header("customerId", 1)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }
}