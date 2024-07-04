package com.io.hhplus.concert.presentation.concert.controller;

import com.io.hhplus.concert.presentation.concert.dto.response.GetPerformanceResponseBody;
import com.io.hhplus.concert.presentation.concert.dto.response.GetSeatResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("concerts")
public class ConcertController {

    /**
     * 예약가능날짜조회
     * @param concertId 콘서트_ID
     * @return 예약 가능 날짜 공연 목록
     */
    @GetMapping("/{concertId}/performances")
    public ResponseEntity<Map<String, Object>> performances(@PathVariable("concertId") Long concertId) {
        long performanceId = 1;
        Date performAt = new Date();
        BigDecimal price = BigDecimal.TEN;
        int capacityLimit = 30;
        List<GetPerformanceResponseBody> performances = List.of(new GetPerformanceResponseBody(performanceId, performAt, price, capacityLimit));
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("performances", performances);
        return ResponseEntity.ok().body(bodyMap);
    }

    /**
     * 예약가능 좌석 조회
     * @param concertId 콘서트_ID
     * @param performanceId 공연_ID
     * @return 예약가능 좌석 목록
     */
    @GetMapping("/{concertId}/performances/{performanceId}/seats")
    public ResponseEntity<Map<String, Object>> seats(@PathVariable("concertId") Long concertId,
                                                     @PathVariable("performanceId") Long performanceId) {
        long seatId = 1;
        String seatNo = "50";
        List<GetSeatResponseBody> seats = List.of(new GetSeatResponseBody(seatId, seatNo));
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("seats", seats);
        return ResponseEntity.ok().body(bodyMap);
    }
}
