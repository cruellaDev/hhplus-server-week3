package com.io.hhplus.concert.domain.customer;

import com.io.hhplus.concert.common.enums.PointType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CustomerValidator {

    private final CustomerRepository customerRepository;

    public boolean isAvailableCustomerId(Long customerId) {
        return customerId != null && customerId.compareTo(0L) > 0;
    }

    public boolean isNotAvailableCustomerId(Long customerId) {
        return !this.isAvailableCustomerId(customerId);
    }

    public boolean isAvailableCustomerName(String customerName) {
        return customerName != null && !customerName.isBlank() && !customerName.equals("N/A");
    }

    public boolean isNotAvailableCustomerName(String customerName) {
        return !this.isAvailableCustomerName(customerName);
    }

    public boolean isSufficientPointAmount(BigDecimal pointAmount) {
        return pointAmount != null && pointAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isInsufficientPointAmount(BigDecimal pointAmount) {
        return !this.isSufficientPointAmount(pointAmount);
    }

    public boolean isAvailablePointTypeWhenCharge(PointType pointType) {
        return pointType != null && pointType.equals(PointType.CHARGE);
    }

    public boolean isNotAvailablePointTypeWhenCharge(PointType pointType) {
        return !this.isAvailablePointTypeWhenCharge(pointType);
    }

}
