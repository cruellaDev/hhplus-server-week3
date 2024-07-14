package com.io.hhplus.concert.domain.concert.service;

import com.io.hhplus.concert.domain.concert.model.Concert;
import com.io.hhplus.concert.domain.concert.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    /**
     * 예약 가능 콘서트 단건 조회
     * @param concertId 콘서트_ID
     * @return 콘서트 정보
     */
    public Concert getAvailableConcert(Long concertId) {
        return concertRepository.findAvailableOneById(concertId).orElseGet(Concert::noContents);
    }

    /**
     * 콘서트 예약 가능 검증
     * @param concert 콘서트 정보
     * @return 예약 가능 여부
     */
    public boolean meetsIfConcertToBeReserved(Concert concert) {
        return concert != null
                && Concert.isAvailableConcertId(concert.concertId())
                && Concert.isAvailableConcertName(concert.concertName())
                && Concert.isAvailableStatus(concert.concertStatus())
                ;
    }
}
