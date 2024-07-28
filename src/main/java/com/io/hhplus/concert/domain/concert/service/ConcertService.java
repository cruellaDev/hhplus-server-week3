package com.io.hhplus.concert.domain.concert.service;

import com.io.hhplus.concert.application.concert.dto.HeldSeatServiceResponse;
import com.io.hhplus.concert.application.concert.dto.HoldSeatServiceRequest;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.domain.concert.model.*;
import com.io.hhplus.concert.domain.concert.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.LongStream;


@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    /**
     * 콘서트 목록 조회
     * @return 콘서트 목록
     */
    public List<Concert> getConcerts() {
        return concertRepository.findConcerts()
                .stream()
                .filter(Concert::isNotDeleted)
                .toList();
    }

    /**
     * 현재일시 기준 특정 콘서트의 예약 가능 공연 목록 조회
     * @param concertId 콘서트 ID
     * @return 현재일시 기준 특정 콘서트의 예약가능 공연 목록 조회
     */
    public List<Performance> getAvailablePerformances(Long concertId) {
        log.info("[service-concert-get-performances] - concertId : {}", concertId);
        return concertRepository.findPerformances(concertId)
                .stream()
                .filter(performance
                        -> performance.isToBePerformed()
                        && performance.isNotDeleted())
                .toList();
    }

    /**
     * 현재일시 기준 특정 콘서트의 공연 구역 목록 조회
     * @param concertId 콘서트_ID
     * @param performanceId 공연_ID
     * @return 구역 목록
     */
    public List<Area> getAvailableAreas(Long concertId, Long performanceId) {
        log.info("[service-concert-get-areas] - concertId : {}, performanceId : {}", concertId, performanceId);
        return concertRepository.findAreas(concertId, performanceId)
                .stream()
                .filter(Area::isNotDeleted)
                .toList();
    }

    /**
     * 특정 콘서트의 공연 구역 예약 가능 좌석 목록 조회
     * @param concertId 콘서트_ID
     * @param performanceId 공연_ID
     * @param areaId 구역_ID
     * @return 예약 가능 좌석 목록
     */
    public List<Seat> getAvailableSeats(Long concertId, Long performanceId, Long areaId) {
        Area area = concertRepository.findArea(concertId, performanceId, areaId).orElseThrow(() -> new CustomException(ResponseMessage.NOT_FOUND));
        log.info("[service-concert-get-area] - concertId : {}, performanceId : {}, areaId : {}", concertId, performanceId, areaId);
        long lStart = 1;
        long lEnd = area.seatCapacity();
        return LongStream.rangeClosed(lStart, lEnd)
                .mapToObj(lValue -> {
                    String seatNumber = area.areaName() + lValue;
                    Boolean isAvailable = concertRepository.existsAvailableSeat(area.concertId(), area.performanceId(), area.areaId(), seatNumber);
                    return isAvailable ? Seat.createAvailableSeat(area, lValue) : Seat.createNotAvailableSeat(area, lValue);
                })
                .toList();
    }

    /**
     * 좌석 배정
     */
    @Transactional
    public List<HeldSeatServiceResponse> holdSeats(HoldSeatServiceRequest serviceRequest) {
        // 좌석 확인
        serviceRequest.getSeats()
                .forEach(seatRequest -> {
                    Boolean isAvailable = concertRepository.existsAvailableSeat(serviceRequest.getConcertId(), serviceRequest.getPerformanceId(), serviceRequest.getAreaId(), seatRequest.getSeatNumber());
                    if (!isAvailable) throw new CustomException(ResponseMessage.SEAT_TAKEN);
                });
        // 콘서트 정보 조회
        Concert concert = concertRepository.findConcert(serviceRequest.getConcertId())
                .filter(o -> o.isAbleToBook() && o.isAvailableConcertStatus() && o.isNotDeleted())
                .orElseThrow(() -> new CustomException(ResponseMessage.CONCERT_NOT_FOUND));
        Performance performance = concertRepository.findPerformance(serviceRequest.getPerformanceId())
                .filter(o -> o.isToBePerformed() && o.isNotDeleted())
                .orElseThrow(() -> new CustomException(ResponseMessage.NOT_FOUND));
        Area area = concertRepository.findArea(serviceRequest.getAreaId())
                .filter(Area::isNotDeleted)
                .orElseThrow(() -> new CustomException(ResponseMessage.NOT_FOUND));
        // 예약 저장
        Reservation reservation = concertRepository.saveReservation(Reservation.create(serviceRequest.getCustomerId()));
        // 티켓 저장
        return serviceRequest.getSeats()
                .stream()
                .map(seatRequest -> HeldSeatServiceResponse.from(concertRepository.saveTicket(Ticket.create(reservation, concert, performance, area, seatRequest.getSeatNumber()))))
                .toList();
    }

    /**
     * TODO 예약 완료
     */
    @Transactional
    public Object confirmReservation(Object object) {
        // 수령인 정보
        // reservedAt not null
        // isReceiveOnline == true -> publishedAt not null, receivedAt not null
        return null;
    }
}
