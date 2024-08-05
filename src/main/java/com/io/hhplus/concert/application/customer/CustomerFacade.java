package com.io.hhplus.concert.application.customer;

import com.io.hhplus.concert.domain.customer.CustomerCommand;
import com.io.hhplus.concert.domain.customer.CustomerService;
import com.io.hhplus.concert.interfaces.customer.dto.CustomerPointDto;
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
    public CustomerPointDto.CustomerPointBalanceResponse getCustomerPointBalance(Long customerId) {
        return CustomerPointDto.CustomerPointBalanceResponse.from(customerService.getCustomerPointBalance(customerId));
    }

    /**
     * 고객 포인트 충전
     * @param command 고객 포인트 충전 command
     * @return 응답 정보
     */
    public CustomerPointDto.ChargeCustomerPointResponse chargeCustomerPoint(CustomerCommand.ChargeCustomerPointCommand command) {
        return CustomerPointDto.ChargeCustomerPointResponse.from(customerService.chargeCustomerPoint(command));
    }

    /**
     * 고객 포인트 충전 (비관적 락 사용)
     * @param command 고객 포인트 충전 command
     * @return 응답 정보
     */
    public CustomerPointDto.ChargeCustomerPointResponse chargeCustomerPointWithPessimisticLick(CustomerCommand.ChargeCustomerPointCommand command) {
        return CustomerPointDto.ChargeCustomerPointResponse.from(customerService.chargeCustomerPointWithPessimisticLock(command));
    }
}
