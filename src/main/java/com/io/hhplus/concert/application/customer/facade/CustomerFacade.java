package com.io.hhplus.concert.application.customer.facade;

import com.io.hhplus.concert.application.customer.dto.CustomerPointHistoryResponse;
import com.io.hhplus.concert.application.customer.dto.CustomerPointRequest;
import com.io.hhplus.concert.application.customer.dto.CustomerResponse;
import com.io.hhplus.concert.common.enums.PointType;
import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.model.CustomerPointHistory;
import com.io.hhplus.concert.domain.customer.service.CustomerService;
import com.io.hhplus.concert.common.exceptions.IllegalArgumentCustomException;
import com.io.hhplus.concert.common.exceptions.IllegalStateCustomException;
import com.io.hhplus.concert.common.exceptions.ResourceNotFoundCustomException;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CustomerFacade {

    private final CustomerService customerService;

    /**
     * 고객 포인트 조회
     * @param customerId 고객_ID
     * @return 응답 정보
     */
    public CustomerResponse getCustomerPoint(Long customerId) {
        if (!Customer.isAvailableCustomerId(customerId)) {
            throw new IllegalArgumentCustomException("고객 ID 값이 잘못 되었습니다.", ResponseMessage.INVALID);
        }

        Customer customer;
        boolean isAvailable;

        // 고객 조회
        customer = customerService.getAvailableCustomer(customerId);
        // 고객 검증
        isAvailable = customerService.meetsIfCustomerValid(customer);
        if (!isAvailable) {
            throw new ResourceNotFoundCustomException("존재하지 않는 고객입니다.", ResponseMessage.NOT_AVAILABLE);
        }

        // 고객 포인트 잔액 조회
        BigDecimal customerPointBalance = customerService.getCustomerPointBalance(customerId);

        // 응답 정보 반환
        customer = Customer.create(customer.customerId(), customer.customerName(), customerPointBalance);
        return new CustomerResponse(customer);
    }

    /**
     * 고객 포인트 충전
     * @param customerPointRequest 요청 정보
     * @return 응답 정보
     */
    public CustomerPointHistoryResponse chargeCustomerPoint(CustomerPointRequest customerPointRequest) {
        if (customerPointRequest == null) {
            throw new IllegalArgumentCustomException("고객 포인트 충전 요청정보가 없습니다.", ResponseMessage.NOT_FOUND);
        }
        if (!Customer.isAvailableCustomerId(customerPointRequest.getCustomerId())) {
            throw new IllegalArgumentCustomException("고객 ID 값이 잘못 되었습니다.", ResponseMessage.INVALID);
        }
        if (!CustomerPointHistory.isInSufficientPointAmount(customerPointRequest.getPointAmount())) {
            throw new IllegalArgumentCustomException("금액은 0보다 큰 값이어야 합니다.", ResponseMessage.INVALID);
        }

        Customer customer;
        CustomerPointHistory customerPointHistory;
        boolean isAvailable;
        CustomerPointHistory savedCustomerPointHistory;
        BigDecimal customerPointBalance;

        // 고객 조회
        customer = customerService.getAvailableCustomer(customerPointRequest.getCustomerId());
        // 고객 검증
        isAvailable = customerService.meetsIfCustomerValid(customer);
        if (!isAvailable) {
            throw new ResourceNotFoundCustomException("존재하지 않는 고객입니다.", ResponseMessage.NOT_AVAILABLE);
        }

        // 충전 포인트 검증
        customerPointHistory = CustomerPointHistory.create(customerPointRequest.getCustomerId(), customerPointRequest.getPointAmount(), PointType.CHARGE, null);
        isAvailable = customerService.meetsIfPointValidBeforeCharge(customerPointHistory);
        if (!isAvailable) {
            throw new IllegalStateCustomException("충전이 불가능합니다.", ResponseMessage.FAIL);
        }

        // 포인트 충전
        savedCustomerPointHistory = customerService.saveCustomerPointHistory(customerPointHistory);

        // 고객 포인트 잔액 조회
        customerPointBalance = customerService.getCustomerPointBalance(customerPointRequest.getCustomerId());

        // 응답 정보 반환
        customer = Customer.create(customer.customerId(), customer.customerName(), customerPointBalance);
        return new CustomerPointHistoryResponse(customer, savedCustomerPointHistory);
    }
}
