package com.io.hhplus.concert.interfaces.queue.controller;

import com.io.hhplus.concert.application.queue.QueueTokenFacade;
import com.io.hhplus.concert.common.GlobalConstants;
import com.io.hhplus.concert.common.dto.CommonResponse;
import com.io.hhplus.concert.domain.queue.dto.BankCounterQueueTokenInfo;
import com.io.hhplus.concert.interfaces.queue.dto.QueueTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("queues")
public class QueueTokenController {

    private final QueueTokenFacade queueTokenFacade;

    /**
     * 대기열 토큰 발급 및 조회
     * @param requestBody 요청 정보
     * @return 응답 정보
     */
    @PostMapping("/issue")
    public ResponseEntity<CommonResponse<QueueTokenDto.BankCounterIssueTokenResponse>> issueToken(@RequestBody QueueTokenDto.BankCounterIssueTokenRequest requestBody) {
        BankCounterQueueTokenInfo bankCounterQueueTokenInfo = queueTokenFacade.issueToken(requestBody.toCommand());
        QueueTokenDto.BankCounterIssueTokenResponse bankCounterIssueTokenResponse = QueueTokenDto.BankCounterIssueTokenResponse.from(bankCounterQueueTokenInfo);

        // 발급된 토큰 헤더에 리턴
        HttpHeaders headers = new HttpHeaders();
        headers.add(GlobalConstants.HEADER_AUTHORIZATION, GlobalConstants.PREFIX_BEARER + bankCounterQueueTokenInfo.queueToken().toString());

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(CommonResponse.success(bankCounterIssueTokenResponse));
    }
}
