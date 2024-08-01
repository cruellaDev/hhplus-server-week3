package com.io.hhplus.concert.interfaces.concert.controller;

import com.io.hhplus.concert.application.concert.ConcertFacade;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ConcertController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Disabled
class ConcertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConcertFacade concertFacade;

//    @Test
//    void concerts() throws Exception {
//        //given
//        ConcertsInfo response = ConcertsInfo.builder().build();
//        given(concertFacade.getAvailableConcerts()).willReturn(response);
//
//        //when - then
//        mockMvc.perform(get("/concerts/")
//                        .header("Authorization", "Bearer " + 1)
//                        .header("customerId", 1)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.responseStatus").value("OK"))
//                .andExpect(jsonPath("$.message").value("OK"))
//                .andExpect(jsonPath("$.data").exists());
//    }
//
//    @Test
//    void performances() throws Exception {
//        //given
//        ConcertInfoWithPerformance response = ConcertInfoWithPerformance.builder().build();
//        given(concertFacade.getAvailablePerformances(anyLong())).willReturn(response);
//
//        //when - then
//        mockMvc.perform(get("/concerts/{concertId}/performances", 1)
//                        .header("Authorization", "Bearer " + 1)
//                        .header("customerId", 1)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.responseStatus").value("OK"))
//                .andExpect(jsonPath("$.message").value("OK"))
//                .andExpect(jsonPath("$.data").exists());
//    }
//
//    @Test
//    void seats() throws Exception {
//        //given
//        ConcertInfoWithPerformanceAndSeats response = ConcertInfoWithPerformanceAndSeats.builder().build();
//        given(concertFacade.getAvailableSeats(anyLong(), anyLong())).willReturn(response);
//
//        //when - then
//        mockMvc.perform(get("/concerts/{concertId}/performances/{performanceId}/seats", 1, 1)
//                        .header("Authorization", "Bearer " + 1)
//                        .header("customerId", 1)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.responseStatus").value("OK"))
//                .andExpect(jsonPath("$.message").value("OK"))
//                .andExpect(jsonPath("$.data").exists());
//    }
}