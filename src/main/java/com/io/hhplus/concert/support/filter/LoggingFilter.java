package com.io.hhplus.concert.support.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String uuid = UUID.randomUUID().toString();

        // request 로그 처리
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);

        String httpMethod = request.getMethod();
        String requestUrl = request.getRequestURI();
        String requestBody = requestWrapper.getContentAsString();

        log.info("traceId : {}, method : {}, url : {}, requestBody: {}", uuid, httpMethod, requestUrl, requestBody);

        // response 로그 처리
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        String responseBody = new String(responseWrapper.getContentAsByteArray(), responseWrapper.getCharacterEncoding());
        log.info("traceId : {}, method : {}, url : {}, responseBody: {}", uuid, httpMethod, requestUrl, responseBody);

        responseWrapper.copyBodyToResponse();

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
