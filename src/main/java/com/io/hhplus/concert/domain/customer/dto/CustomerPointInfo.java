package com.io.hhplus.concert.domain.customer.dto;

import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.model.CustomerPointHistory;
import lombok.Builder;

@Builder
public record CustomerPointInfo(
    Customer customer,
    CustomerPointHistory customerPointHistory
) {
    public static CustomerPointInfo of(Customer customer, CustomerPointHistory customerPointHistory) {
        return CustomerPointInfo.builder()
                .customer(customer)
                .customerPointHistory(customerPointHistory)
                .build();
    }
}
