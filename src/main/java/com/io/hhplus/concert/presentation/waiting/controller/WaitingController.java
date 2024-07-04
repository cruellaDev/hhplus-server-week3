package com.io.hhplus.concert.presentation.waiting.controller;

import com.io.hhplus.concert.presentation.waiting.dto.request.PostWaitingQueueRequestBody;
import com.io.hhplus.concert.presentation.waiting.dto.response.PostWaitingQueueResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("waiting")
@RestController
public class WaitingController {

    /**
     * 대기열 진입 (토큰 발급)
     * @param requestBody 요청 정보
     * @return 응답 정보
     */
    @PostMapping("/enter")
    public ResponseEntity<PostWaitingQueueResponseBody> enter(@RequestBody PostWaitingQueueRequestBody requestBody) {
        return ResponseEntity.ok().body(new PostWaitingQueueResponseBody(requestBody.customerId(), 3L, "활성됨", 2000));
    }

    /**
     * 대기열 확인 (토큰 조회)
     * @param requestBody 요청 정보
     * @return 응답 정보
     */
    @PostMapping("check")
    public ResponseEntity<PostWaitingQueueResponseBody> check(@RequestBody PostWaitingQueueRequestBody requestBody) {
        return ResponseEntity.ok().body(new PostWaitingQueueResponseBody(requestBody.customerId(), 3L, "만료됨", 0));
    }
}
