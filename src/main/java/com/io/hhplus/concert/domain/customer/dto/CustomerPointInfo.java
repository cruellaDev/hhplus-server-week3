package com.io.hhplus.concert.domain.customer.dto;

import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.model.CustomerPointHistory;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomerPointInfo {
    private Customer customer;
    private CustomerPointHistory customerPointHistory;

    public static CustomerPointInfo of(Customer customer, CustomerPointHistory customerPointHistory) {
        return CustomerPointInfo.builder()
                .customer(customer)
                .customerPointHistory(customerPointHistory)
                .build();
    }
}
