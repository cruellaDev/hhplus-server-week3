package com.io.hhplus.concert.domain.concert.service;

import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.concert.model.Performance;
import com.io.hhplus.concert.domain.concert.repository.PerformanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PerformanceServiceTest {

    @Mock
    private PerformanceRepository performanceRepository;

    @InjectMocks
    private PerformanceService performanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 공연 목록 반환
     */
    @Test
    void getAvailablePerformances() {
        //given
        List<Performance> performances = List.of(
                Performance.create(1L, BigDecimal.valueOf(30000), 50, DateUtils.createTemporalDateByIntParameters(2024, 9, 22, 13, 0, 0)),
                Performance.create(2L, BigDecimal.valueOf(100000), 50, DateUtils.createTemporalDateByIntParameters(2024, 12, 22, 13, 0, 0))
        );
        given(performanceRepository.findAvailableAllByConcertId(anyLong())).willReturn(performances);

        //when
        List<Performance> result = performanceService.getAvailablePerformances(3L);

        //then
        assertThat(result).hasSize(2);
    }

    /**
     * 공연 단건 반환
     */
    @Test
    void getAvailablePerformance() {
        Performance performance = Performance.create(1L, BigDecimal.valueOf(30000), 50, DateUtils.createTemporalDateByIntParameters(2024, 9, 22, 13, 0, 0));

        given(performanceRepository.findAvailableOneByConcertIdAndPerformanceId(anyLong(), anyLong())).willReturn(Optional.of(performance));

        //when
        Performance result = performanceService.getAvailablePerformance(3L, 4L);

        //then
        assertAll(() -> assertEquals(performance.performanceId(), result.performanceId()),
                () -> assertEquals(performance.price(), result.price()),
                () -> assertEquals(performance.capacityLimit(), result.capacityLimit()),
                () -> assertEquals(performance.performedAt(), result.performedAt()));
    }

    /**
     * 공연예약 검증 다 통과 시
     */
    @Test
    void meetsIfPerformanceToBeReserved_when_valid() {
        // given
        Performance performance = Performance.create(
                1L,
                BigDecimal.valueOf(30000),
                50,
                DateUtils.createTemporalDateByIntParameters(2024,11, 20, 14 ,0,0));

        // when
        boolean isValid = performanceService.meetsIfPerformanceToBeReserved(performance);

        // then
        assertThat(isValid).isTrue();
    }

    /**
     * 공연예약 검증 공연ID 이상할 시
     */
    @Test
    void meetsIfPerformanceToBeReserved_when_performanceId_is_wrong() {
        // given
        Performance performance = Performance.create(
                -1L,
                BigDecimal.valueOf(30000),
                50,
                DateUtils.createTemporalDateByIntParameters(2024,11, 20, 14 ,0,0));

        // when
        boolean isValid = performanceService.meetsIfPerformanceToBeReserved(performance);

        // then
        assertThat(isValid).isFalse();
    }

    /**
     * 공연예약 검증 인원 이상할 시
     */
    @Test
    void meetsIfPerformanceToBeReserved_when_capacity_is_wrong() {
        // given
        Performance performance = Performance.create(
                1L,
                BigDecimal.valueOf(30000),
                -50,
                DateUtils.createTemporalDateByIntParameters(2024,11, 20, 14 ,0,0));

        // when
        boolean isValid = performanceService.meetsIfPerformanceToBeReserved(performance);

        // then
        assertThat(isValid).isFalse();
    }

    /**
     * 공연예약 검증 공연일시 현재일시보다 이전일 시
     */
    @Test
    void meetsIfPerformanceToBeReserved_when_performedAt_is_wrong() {
        // given
        Performance performance = Performance.create(
                1L,
                BigDecimal.valueOf(30000),
                50,
                DateUtils.createTemporalDateByIntParameters(2023,11, 20, 14 ,0,0));

        // when
        boolean isValid = performanceService.meetsIfPerformanceToBeReserved(performance);

        // then
        assertThat(isValid).isFalse();
    }

}