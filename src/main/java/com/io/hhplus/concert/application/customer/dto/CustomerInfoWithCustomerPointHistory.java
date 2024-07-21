package com.io.hhplus.concert.application.customer.dto;

import com.io.hhplus.concert.domain.customer.service.model.CustomerModel;
import com.io.hhplus.concert.domain.customer.service.model.CustomerPointHistoryModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CustomerInfoWithCustomerPointHistory {

    private final CustomerModel customer;
    private final CustomerPointHistoryModel customerPointHistory;
}
