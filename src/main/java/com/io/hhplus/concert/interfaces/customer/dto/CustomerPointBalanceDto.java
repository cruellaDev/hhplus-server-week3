package com.io.hhplus.concert.interfaces.customer.dto;

import com.io.hhplus.concert.application.customer.dto.CustomerPointBalanceServiceResponse;
import lombok.Builder;

import java.math.BigDecimal;

public class CustomerPointBalanceDto {

    @Builder
    public static class Response {
        private Long customerId;
        private BigDecimal pointBalance;

        public static CustomerPointBalanceDto.Response from(CustomerPointBalanceServiceResponse customerPointBalanceServiceResponse) {
            return Response.builder()
                    .customerId(customerPointBalanceServiceResponse.customerId())
                    .pointBalance(customerPointBalanceServiceResponse.pointBalance())
                    .build();
        }
    }
}
