package com.io.hhplus.concert.interfaces.token.controller;

import com.io.hhplus.concert.common.GlobalConstants;
import com.io.hhplus.concert.common.dto.CommonResponse;
import com.io.hhplus.concert.domain.queue.TokenService;
import com.io.hhplus.concert.domain.queue.dto.BankCounterQueueTokenInfo;
import com.io.hhplus.concert.interfaces.token.dto.QueueTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("queues")
public class TokenController {

    private final TokenService tokenService;

    /**
     * 대기열 토큰 발급 및 조회
     * @param requestBody 요청 정보
     * @return 응답 정보
     */
    @PostMapping("/issue")
    public ResponseEntity<CommonResponse<QueueTokenDto.BankCounterIssueTokenResponse>> issueToken(@RequestBody QueueTokenDto.BankCounterIssueTokenRequest requestBody) {
        BankCounterQueueTokenInfo bankCounterQueueTokenInfo = tokenService.issueToken(requestBody.toCommand());
        QueueTokenDto.BankCounterIssueTokenResponse bankCounterIssueTokenResponse = QueueTokenDto.BankCounterIssueTokenResponse.from(bankCounterQueueTokenInfo);

        // 발급된 토큰 헤더에 리턴
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, GlobalConstants.PREFIX_BEARER + bankCounterQueueTokenInfo.queueToken().toString());

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(CommonResponse.success(bankCounterIssueTokenResponse));
    }
}
