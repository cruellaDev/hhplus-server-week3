package com.io.hhplus.concert.interfaces.customer.dto;

import com.io.hhplus.concert.domain.customer.CustomerCommand;
import com.io.hhplus.concert.domain.customer.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class CustomerDto {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterCustomerResponse {
        private Long customerId;
        private String customerName;
        private BigDecimal pointBalance;

        public static CustomerDto.RegisterCustomerResponse from(Customer customer) {
            return CustomerDto.RegisterCustomerResponse.builder()
                    .customerId(customer.customerId())
                    .customerName(customer.customerName())
                    .pointBalance(customer.pointBalance())
                    .build();
        }
    }

    @Data
    public static class RegisterCustomerRequest {
        private String customerName;

        public CustomerCommand.RegisterCustomerCommand toCommand() {
            return CustomerCommand.RegisterCustomerCommand.builder()
                    .customerName(this.customerName)
                    .build();
        }
    }
}
