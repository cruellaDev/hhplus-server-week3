package com.io.hhplus.concert.support.interceptor;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.domain.customer.service.CustomerValidator;
import com.io.hhplus.concert.domain.waiting.service.WaitingValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class TokenInterceptor implements HandlerInterceptor {

    private final CustomerValidator customerValidator;
    private final WaitingValidator waitingValidator;

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || authorization.isBlank()) {
            throw new CustomException(ResponseMessage.TOKEN_NOT_FOUNT, "토큰이 존재하지 않습니다.");
        }

        String token = authorization.replaceAll("Bearer ", "");

        // 고객 검증
        customerValidator.validateCustomer(token);

        // 대기열 토큰 검증
        waitingValidator.validateToken(token);

        return true;
    }

}
