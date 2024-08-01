package com.io.hhplus.concert.support.interceptor;

import com.io.hhplus.concert.application.queue.QueueTokenFacade;
import com.io.hhplus.concert.common.GlobalConstants;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class TokenInterceptor implements HandlerInterceptor {

    private QueueTokenFacade queueTokenFacade;

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || authorization.isBlank()) {
            throw new CustomException(ResponseMessage.TOKEN_NOT_FOUNT);
        }

        UUID token = UUID.fromString(authorization.replaceAll(GlobalConstants.PREFIX_BEARER, ""));

        queueTokenFacade.validateToken(token);

        return true;
    }

}
