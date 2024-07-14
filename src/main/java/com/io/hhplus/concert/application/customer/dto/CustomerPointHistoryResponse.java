package com.io.hhplus.concert.application.customer.dto;

import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.model.CustomerPointHistory;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomerPointHistoryResponse extends CustomerResponse {

    private final CustomerPointHistory customerPointHistory;

    public CustomerPointHistoryResponse(Customer customer, CustomerPointHistory customerPointHistory) {
        super(customer);
        this.customerPointHistory = customerPointHistory;
    }
}
