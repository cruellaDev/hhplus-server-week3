package com.io.hhplus.concert.interfaces.customer.dto;

import com.io.hhplus.concert.application.customer.dto.ChargeCustomerPointServiceResponse;
import com.io.hhplus.concert.domain.customer.dto.ChargeCustomerPointServiceRequest;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

public class ChargeCustomerPointDto {

    public static class Request {
        private Long customerId;
        private BigDecimal amount;

        public ChargeCustomerPointServiceRequest toServiceRequest() {
            return ChargeCustomerPointServiceRequest.builder()
                    .customerId(this.customerId)
                    .amount(this.amount)
                    .build();
        }
    }

    @Builder
    public static class Response {
        private Long customerId;
        private BigDecimal pointBalance;
        private BigDecimal chargeAmount;
        private Date chargedAt;

        public static ChargeCustomerPointDto.Response from(ChargeCustomerPointServiceResponse chargeCustomerPointServiceResponse) {
            return Response.builder()
                    .customerId(chargeCustomerPointServiceResponse.customerId())
                    .pointBalance(chargeCustomerPointServiceResponse.pointBalance())
                    .chargeAmount(chargeCustomerPointServiceResponse.chargeAmount())
                    .chargedAt(chargeCustomerPointServiceResponse.chargedAt())
                    .build();
        }
    }
}
