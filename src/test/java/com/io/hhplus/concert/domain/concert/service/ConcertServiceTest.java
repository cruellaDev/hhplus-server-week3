package com.io.hhplus.concert.domain.concert.service;

import com.io.hhplus.concert.common.enums.ConcertStatus;
import com.io.hhplus.concert.domain.concert.model.Concert;
import com.io.hhplus.concert.domain.concert.repository.ConcertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertService concertService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 예약 가능 콘서트 단건 조회
     */
    @Test
    void getAvailableConcert() {
        // given
        Concert concert = Concert.create(1L, "허재코치님 팬미팅", "허재", ConcertStatus.AVAILABLE);
        given(concertRepository.findAvailableOneById(anyLong())).willReturn(Optional.of(concert));

        // when
        Concert result = concertService.getAvailableConcert(1L);

        // then
        assertAll(() -> assertEquals(concert.concertId(), result.concertId()),
                () -> assertEquals(concert.concertName(), result.concertName()),
                () -> assertEquals(concert.artistName(), result.artistName()),
                () -> assertEquals(concert.concertStatus(), result.concertStatus()));
    }

    /**
     * 콘서트 검증 모두 다 통과 시
     */
    @Test
    void meetsIfConcertToBeReserved_when_passAll() {
        Concert concert = Concert.create(1L, "허재코치님 팬미팅", "허재", ConcertStatus.AVAILABLE);

        boolean isValid = concertService.meetsIfConcertToBeReserved(concert);

        assertThat(isValid).isTrue();
    }

    /**
     * 콘서트 검증 콘서트ID 이상할 시
     */
    @Test
    void meetsIfConcertToBeReserved_when_concertId_is_wrong() {
        Concert concert = Concert.create(-1L, "허재코치님 팬미팅", "허재", ConcertStatus.AVAILABLE);

        boolean isValid = concertService.meetsIfConcertToBeReserved(concert);

        assertThat(isValid).isFalse();
    }

    /**
     * 콘서트 검증 콘서트명 이상할 시
     */
    @Test
    void meetsIfConcertToBeReserved_when_concertName_is_wrong() {
        Concert concert = Concert.create(1L, "", "허재", ConcertStatus.AVAILABLE);

        boolean isValid = concertService.meetsIfConcertToBeReserved(concert);

        assertThat(isValid).isFalse();
    }

    /**
     * 콘서트 검증 콘서트상태 이상할 시
     */
    @Test
    void meetsIfConcertToBeReserved_when_concertStatus_is_wrong() {
        Concert concert = Concert.create(1L, "허재코치님 팬미팅", "허재", ConcertStatus.CLOSED);

        boolean isValid = concertService.meetsIfConcertToBeReserved(concert);

        assertThat(isValid).isFalse();
    }
}