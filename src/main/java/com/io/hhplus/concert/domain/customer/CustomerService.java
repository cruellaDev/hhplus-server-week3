package com.io.hhplus.concert.domain.customer;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.domain.customer.dto.CustomerPointInfo;
import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.model.CustomerPointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * 고객 등록
     * @param command 고객 등록 command
     * @return 고객 등록 command
     */
    public Customer regiterCustomer(CustomerCommand.RegisterCustomerCommand command) {
        return customerRepository.saveCustomer(Customer.create().register(command));
    }

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
    public CustomerPointInfo chargeCustomerPointWithPessimisticLock(CustomerCommand.ChargeCustomerPointCommand command){
        Customer customer = customerRepository.findAvailableCustomerWithPessimisticLock(command.getCustomerId())
                .filter(o
                        -> o.isNotDreamed()
                        && o.isNotWithdrawn()
                        && o.isNotDeleted())
                .orElseThrow(() -> new CustomException(ResponseMessage.CUSTOMER_NOT_FOUND));
        return CustomerPointInfo.of(
                customerRepository.saveCustomer(customer.chargePoint(command)),
                customerRepository.saveCustomerPointHistory(CustomerPointHistory.chargePoint(command))
        );
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
                customerRepository.saveCustomer(customer.chargePoint(command)),
                customerRepository.saveCustomerPointHistory(CustomerPointHistory.chargePoint(command))
        );
    }

    /**
     * 고객 포인트 사용
     * @param command 고객 포인트 충전 command
     * @return 고객 포인트 사용 정보
     */
    @Transactional
    public CustomerPointInfo useCustomerPoint(CustomerCommand.UseCustomerPointCommand command) {
        Customer customer = customerRepository.findAvailableCustomer(command.getCustomerId())
                .filter(o
                        -> o.isNotDreamed()
                        && o.isNotWithdrawn()
                        && o.isNotDeleted())
                .orElseThrow(() -> new CustomException(ResponseMessage.CUSTOMER_NOT_FOUND));
        return CustomerPointInfo.of(
                customerRepository.saveCustomer(customer.usePoint(command)),
                customerRepository.saveCustomerPointHistory(CustomerPointHistory.usePoint(command)));
    }

    /**
     * 고객 포인트 사용
     * @param command 고객 포인트 충전 command
     * @return 고객 포인트 사용 정보
     */
    @Transactional
    public CustomerPointInfo useCustomerPointWithPessimisticLock(CustomerCommand.UseCustomerPointCommand command) {
        Customer customer = customerRepository.findAvailableCustomer(command.getCustomerId()).orElseThrow(() -> new CustomException(ResponseMessage.CUSTOMER_NOT_FOUND));
        return CustomerPointInfo.of(
                customerRepository.saveCustomer(customer.usePoint(command)),
                customerRepository.saveCustomerPointHistory(CustomerPointHistory.usePoint(command)));
    }
}
