package com.io.hhplus.concert.domain.customer.model;

import com.io.hhplus.concert.common.enums.PointType;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.domain.customer.CustomerCommand;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record CustomerPointHistory(
        Long customerPointHistoryId,
        Long customerId,
        BigDecimal pointAmount,
        PointType pointType,
        Date createdAt,
        Date modifiedAt,
        Date deletedAt
) {
    public static CustomerPointHistory chargePoint(CustomerCommand.ChargeCustomerPointCommand command) {
        if (command.getAmount() == null || command.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomException(ResponseMessage.INVALID, "포인트 충전 금액은 0보다 커야 합니다.");
        }
        return CustomerPointHistory.builder()
                .customerId(command.getCustomerId())
                .pointAmount(command.getAmount())
                .pointType(PointType.CHARGE)
                .build();
    }

    public static CustomerPointHistory usePoint(CustomerCommand.UseCustomerPointCommand command) {
        if (command.getAmount() == null || command.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomException(ResponseMessage.INVALID, "포인트 사용 금액은 0보다 커야 합니다.");
        }
        return CustomerPointHistory.builder()
                .customerId(command.getCustomerId())
                .pointAmount(command.getAmount())
                .pointType(PointType.USE)
                .build();
    }
}
