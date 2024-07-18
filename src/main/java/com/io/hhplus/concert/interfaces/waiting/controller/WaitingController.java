package com.io.hhplus.concert.interfaces.waiting.controller;

import com.io.hhplus.concert.application.waiting.dto.WaitingResultInfo;
import com.io.hhplus.concert.application.waiting.facade.WaitingFacade;
import com.io.hhplus.concert.common.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("waiting")
public class WaitingController {

    private final WaitingFacade waitingFacade;

    /**
     * 대기열 진입 (토큰 발급)
     * @param customerId 고객_ID
     * @return 응답 정보
     */
    @PostMapping("/enter")
    public CommonResponse<WaitingResultInfo> enter(@RequestHeader("customerId") Long customerId) {
        return CommonResponse.success(waitingFacade.publishWaitingToken(customerId));
    }

    /**
     * 대기열 확인 (토큰 조회)
     * @param customerId 고객_ID
     * @return 응답 정보
     */
    @GetMapping("check")
    public CommonResponse<WaitingResultInfo> check(@RequestHeader("customerId") Long customerId) {
        return CommonResponse.success(waitingFacade.getWaitingToken(customerId));
    }
}
