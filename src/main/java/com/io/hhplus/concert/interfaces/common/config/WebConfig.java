package com.io.hhplus.concert.interfaces.common.config;

import com.io.hhplus.concert.support.interceptor.TokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/v1/api/**")                 // 인터셉터 적용 경로
                .excludePathPatterns("/v1/api/queues/**")      // 제외 경로 (토큰)
                .excludePathPatterns("/v1/api/customers/**");  // 제외 경로 (고객)
        ;
    }

}
