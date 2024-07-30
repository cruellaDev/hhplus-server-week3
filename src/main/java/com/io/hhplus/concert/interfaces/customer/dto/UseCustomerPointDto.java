package com.io.hhplus.concert.interfaces.customer.dto;


import com.io.hhplus.concert.application.customer.dto.UseCustomerPointServiceResponse;
import com.io.hhplus.concert.application.customer.dto.UseCustomerPointServiceRequest;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

public class UseCustomerPointDto {

    public static class Request {
        private Long customerId;
        private BigDecimal amount;

        public UseCustomerPointServiceRequest toServiceRequest() {
            return UseCustomerPointServiceRequest.builder()
                    .customerId(this.customerId)
                    .amount(this.amount)
                    .build();
        }
    }

    @Builder
    public static class Response {
        private Long customerId;
        private BigDecimal pointBalance;
        private BigDecimal useAmount;
        private Date usedAt;
    }

    public static UseCustomerPointDto.Response from(UseCustomerPointServiceResponse useCustomerPointServiceResponse) {
        return UseCustomerPointDto.Response.builder()
                .customerId(useCustomerPointServiceResponse.customerId())
                .pointBalance(useCustomerPointServiceResponse.pointBalance())
                .useAmount(useCustomerPointServiceResponse.useAmount())
                .usedAt(useCustomerPointServiceResponse.usedAt())
                .build();
    }

}
