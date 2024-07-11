package com.io.hhplus.concert.application.customer.dto;

import com.io.hhplus.concert.domain.customer.model.Customer;import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponse {
    private final Customer customer;

    public CustomerResponse(Customer customer) {
        this.customer = customer;
    }
}
