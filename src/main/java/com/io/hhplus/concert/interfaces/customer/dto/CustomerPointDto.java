package com.io.hhplus.concert.interfaces.customer.dto;

import com.io.hhplus.concert.domain.customer.CustomerCommand;
import com.io.hhplus.concert.domain.customer.dto.CustomerPointInfo;
import com.io.hhplus.concert.domain.customer.model.Customer;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

public class CustomerPointDto {

    @Builder
    public static class CustomerPointBalanceResponse {
        private Long customerId;
        private BigDecimal pointBalance;

        public static CustomerPointDto.CustomerPointBalanceResponse from(Customer customer) {
            return CustomerPointDto.CustomerPointBalanceResponse.builder()
                    .customerId(customer.customerId())
                    .pointBalance(customer.pointBalance())
                    .build();
        }
    }

    public static class ChargeCustomerPointRequest {
        private Long customerId;
        private BigDecimal amount;

        public CustomerCommand.ChargeCustomerPointCommand toCommand() {
            return CustomerCommand.ChargeCustomerPointCommand.builder()
                    .customerId(this.customerId)
                    .amount(this.amount)
                    .build();
        }
    }

    @Builder
    public static class ChargeCustomerPointResponse {
        private Long customerId;
        private BigDecimal pointBalance;
        private BigDecimal chargeAmount;
        private Date chargedAt;

        public static CustomerPointDto.ChargeCustomerPointResponse from(CustomerPointInfo chargeCustomerPointInfo) {
            return CustomerPointDto.ChargeCustomerPointResponse.builder()
                    .customerId(chargeCustomerPointInfo.getCustomer().customerId())
                    .pointBalance(chargeCustomerPointInfo.getCustomer().pointBalance())
                    .chargeAmount(chargeCustomerPointInfo.getCustomerPointHistory().pointAmount())
                    .chargedAt(chargeCustomerPointInfo.getCustomerPointHistory().createdAt())
                    .build();
        }
    }

    public static class UseCustomerPointRequest {
        private Long customerId;
        private BigDecimal amount;

        public CustomerCommand.UseCustomerPointCommand toCommand() {
            return CustomerCommand.UseCustomerPointCommand.builder()
                    .customerId(this.customerId)
                    .amount(this.amount)
                    .build();
        }
    }

    @Builder
    public static class UseCustomerPointResponse {
        private Long customerId;
        private BigDecimal pointBalance;
        private BigDecimal useAmount;
        private Date usedAt;

        public static CustomerPointDto.UseCustomerPointResponse from(CustomerPointInfo useCustomerPointInfo) {
            return CustomerPointDto.UseCustomerPointResponse.builder()
                    .customerId(useCustomerPointInfo.getCustomer().customerId())
                    .pointBalance(useCustomerPointInfo.getCustomer().pointBalance())
                    .useAmount(useCustomerPointInfo.getCustomerPointHistory().pointAmount())
                    .usedAt(useCustomerPointInfo.getCustomerPointHistory().createdAt())
                    .build();
        }
    }

}
