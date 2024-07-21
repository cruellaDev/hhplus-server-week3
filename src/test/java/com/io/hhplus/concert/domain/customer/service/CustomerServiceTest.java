package com.io.hhplus.concert.domain.customer.service;

import com.io.hhplus.concert.common.enums.PointType;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.domain.customer.service.model.CustomerModel;
import com.io.hhplus.concert.domain.customer.service.model.CustomerPointHistoryModel;
import com.io.hhplus.concert.domain.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 고객 조회
     */
    @Test
    void getAvailableCustomer() {
        // given
        CustomerModel customerModel = CustomerModel.create(1L, "김항해", BigDecimal.valueOf(10000));
        given(customerRepository.findAvailableOneById(anyLong())).willReturn(Optional.of(customerModel));

        // when
        CustomerModel result = customerService.getAvailableCustomer(1L);

        // then
        assertAll(() -> assertEquals(customerModel.customerId(), result.customerId()),
                () -> assertEquals(customerModel.customerName(), result.customerName()),
                () -> assertEquals(customerModel.pointBalance(), result.pointBalance()));
    }

    /**
     * 고객 포인트 잔액 조회
     */
    @Test
    void getCustomerPointBalance() {
        // given
        BigDecimal pointBalance = BigDecimal.valueOf(30000);
        given(customerRepository.sumCustomerPointBalanceByCustomerId(anyLong())).willReturn(pointBalance);

        // when
        BigDecimal result = customerService.getCustomerPointBalance(1L);

        // then
        assertThat(result).isEqualTo(pointBalance);
    }

    /**
     * 고객 포인트 내역 저장
     */
    @Test
    void saveCustomerPointHistory() {
        // given
        CustomerPointHistoryModel customerPointHistoryModel = CustomerPointHistoryModel.create(
                1L,
                BigDecimal.valueOf(30000),
                PointType.CHARGE,
                null
        );
        given(customerRepository.saveCustomerPointHistory(any(CustomerPointHistoryModel.class))).willReturn(customerPointHistoryModel);

        // when
        CustomerPointHistoryModel result = customerService.saveCustomerPointHistory(customerPointHistoryModel);

        // then
        assertAll(() -> assertEquals(customerPointHistoryModel.customerId(), result.customerId()),
                () -> assertEquals(customerPointHistoryModel.pointAmount(), result.pointAmount()),
                () -> assertEquals(customerPointHistoryModel.pointType(), result.pointType()));
    }
    /**
     * 고객 포인트 사용
     */
    @Test
    void useCustomerPoint() {
        // given
        CustomerPointHistoryModel customerPointHistoryModel = CustomerPointHistoryModel.create(
                1L,
                BigDecimal.valueOf(30000),
                PointType.USE,
                null
        );
        given(customerRepository.saveCustomerPointHistory(any(CustomerPointHistoryModel.class))).willReturn(customerPointHistoryModel);

        // when
        CustomerPointHistoryModel result = customerService.saveCustomerPointHistory(customerPointHistoryModel);

        // then
        assertAll(() -> assertEquals(customerPointHistoryModel.customerId(), result.customerId()),
                () -> assertEquals(customerPointHistoryModel.pointAmount(), result.pointAmount()),
                () -> assertEquals(customerPointHistoryModel.pointType(), result.pointType()));
    }

}