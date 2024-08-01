package com.io.hhplus.concert.domain.customer;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.domain.customer.dto.CustomerPointInfo;
import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.model.CustomerPointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * 고객 포인트 잔액 조회
     * @param customerId 고객 ID
     * @return 포인트잔액
     */
    public Customer getCustomerPointBalance(Long customerId) {
        return customerRepository.findAvailableCustomer(customerId)
                .filter(o
                        -> o.isNotDreamed()
                        && o.isNotWithdrawn()
                        && o.isNotDeleted())
                .orElseThrow(() -> new CustomException(ResponseMessage.CUSTOMER_NOT_FOUND));
    }

    /**
     * 고객 포인트 충전
     * @param command 고객 포인트 충전 command
     * @return 고객 포인트 충전 정보
     */
    @Transactional
    public CustomerPointInfo chargeCustomerPoint(CustomerCommand.ChargeCustomerPointCommand command){
        Customer customer = customerRepository.findAvailableCustomer(command.getCustomerId())
                .filter(o
                        -> o.isNotDreamed()
                        && o.isNotWithdrawn()
                        && o.isNotDeleted())
                .orElseThrow(() -> new CustomException(ResponseMessage.CUSTOMER_NOT_FOUND));
        return CustomerPointInfo.of(
                customerRepository.saveCustomer(customer.chargePoint(command.getAmount())),
                customerRepository.saveCustomerPointHistory(CustomerPointHistory.chargePointOf(customer, command.getAmount()))
        );
    }

    /**
     * 고객 포인트 사용
     * @param command 고객 포인트 충전 command
     * @return 고객 포인트 사용 정보
     */
    @Transactional
    public CustomerPointInfo useCustomerPoint(CustomerCommand.UseCustomerPointCommand command) {
        // TODO PESSIMISTIC_WRITE VS PESSIMISTIC_READ(갱신소실 문제 있음!!) 알기
        // 비관적 락도 row 조회 가능하다! 단 select for update는 row 조회 불가
        Customer customer = customerRepository.findAvailableCustomer(command.getCustomerId())
                .filter(o
                        -> o.isNotDreamed()
                        && o.isNotWithdrawn()
                        && o.isNotDeleted())
                .orElseThrow(() -> new CustomException(ResponseMessage.CUSTOMER_NOT_FOUND));
        return CustomerPointInfo.of(
                customerRepository.saveCustomer(customer.usePoint(command.getAmount())),
                customerRepository.saveCustomerPointHistory(CustomerPointHistory.usePointOf(customer, command.getAmount())));
    }
}
