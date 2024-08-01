package com.io.hhplus.concert.application.customer.facade;

import com.io.hhplus.concert.domain.customer.CustomerCommand;
import com.io.hhplus.concert.domain.customer.CustomerService;
import com.io.hhplus.concert.interfaces.customer.dto.ChargeCustomerPointDto;
import com.io.hhplus.concert.interfaces.customer.dto.CustomerPointBalanceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerFacade {

    private final CustomerService customerService;

    /**
     * 고객 포인트 잔액 조회
     * @param customerId 고객_ID
     * @return 응답 정보
     */
    public CustomerPointBalanceDto.Response getCustomerPointBalance(Long customerId) {
        return CustomerPointBalanceDto.Response.from(customerService.getCustomerPointBalance(customerId));
    }

    /**
     * 고객 포인트 충전
     * @param command 고객 포인트 충전 command
     * @return 응답 정보
     */
    public ChargeCustomerPointDto.Response chargeCustomerPoint(CustomerCommand.ChargeCustomerPointCommand command) {
        return ChargeCustomerPointDto.Response.from(customerService.chargeCustomerPoint(command));
    }
}
