package com.io.hhplus.concert.domain.customer.model;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.domain.customer.CustomerCommand;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Builder
public record Customer(
        Long customerId,
        UUID customerUuid,
        String customerName,
        BigDecimal pointBalance,
        Date dreamedAt,
        Date withdrawnAt,
        Date createdAt,
        Date modifiedAt,
        Date deletedAt
) {

    public boolean isNotDreamed() {
        return this.dreamedAt == null;
    }

    public boolean isNotWithdrawn() {
        return this.withdrawnAt == null;
    }

    public boolean isNotDeleted() {
        return this.deletedAt == null;
    }

    public static Customer create() {
        return Customer.builder().build();
    }

    public Customer register(CustomerCommand.RegisterCustomerCommand command) {
        if (command.getCustomerName() == null || command.getCustomerName().isBlank()) {
            throw new CustomException(ResponseMessage.INVALID, "고객 명이 존재하지 않습니다.");
        }
        return Customer.builder()
                .customerName(command.getCustomerName())
                .pointBalance(BigDecimal.ZERO)
                .build();
    }

    public Customer chargePoint(CustomerCommand.ChargeCustomerPointCommand command) {
        if (command.getAmount() == null || command.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomException(ResponseMessage.INVALID, "포인트 충전 금액은 0보다 커야 합니다.");
        }
        BigDecimal pointBalance = this.pointBalance.add(command.getAmount());
        return Customer.builder()
                .customerId(this.customerId)
                .customerUuid(this.customerUuid)
                .customerName(this.customerName)
                .pointBalance(pointBalance)
                .dreamedAt(this.dreamedAt)
                .withdrawnAt(this.withdrawnAt)
                .createdAt(this.createdAt)
                .modifiedAt(this.modifiedAt)
                .deletedAt(this.deletedAt)
                .build();
    }

    public Customer usePoint(CustomerCommand.UseCustomerPointCommand command) {
        if (command.getAmount() == null || command.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomException(ResponseMessage.INVALID, "포인트 사용 금액은 0보다 커야 합니다.");
        }
        if (this.pointBalance().compareTo(command.getAmount()) < 0) {
            throw new CustomException(ResponseMessage.OUT_OF_BUDGET, "포인트 잔액이 부족합니다.");
        }
        BigDecimal pointBalance = this.pointBalance.subtract(command.getAmount());
        return Customer.builder()
                .customerId(this.customerId)
                .customerUuid(this.customerUuid)
                .customerName(this.customerName)
                .pointBalance(pointBalance)
                .dreamedAt(this.dreamedAt)
                .withdrawnAt(this.withdrawnAt)
                .createdAt(this.createdAt)
                .modifiedAt(this.modifiedAt)
                .deletedAt(this.deletedAt)
                .build();
    }
}
