package com.io.hhplus.concert.support.interceptor;

import com.io.hhplus.concert.application.queue.QueueTokenFacade;
import com.io.hhplus.concert.common.GlobalConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {

    private QueueTokenFacade queueTokenFacade;

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || authorization.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "헤더에 권한이 누락되었습니다.");
        }
        if (!authorization.startsWith(GlobalConstants.PREFIX_BEARER)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 토큰 형식입니다.");
        }

        UUID token;
        try {
            token = UUID.fromString(authorization.replaceAll(GlobalConstants.PREFIX_BEARER, ""));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 토큰 형식입니다.");
        }

        queueTokenFacade.validateToken(token);

        return true;
    }

}
