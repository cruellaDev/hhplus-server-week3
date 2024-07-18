package com.io.hhplus.concert.application.customer.dto;

import com.io.hhplus.concert.domain.customer.service.model.CustomerModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomerInfo {
    private final CustomerModel customer;

    public CustomerInfo(CustomerModel customerModel) {
        this.customer = customerModel;
    }
}
