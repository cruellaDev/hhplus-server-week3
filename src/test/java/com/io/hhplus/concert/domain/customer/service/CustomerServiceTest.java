package com.io.hhplus.concert.domain.customer.service;

import com.io.hhplus.concert.common.enums.PointType;
import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.model.CustomerPointHistory;
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
        Customer customer = Customer.create(1L, "김항해", BigDecimal.valueOf(10000));
        given(customerRepository.findAvailableOneById(anyLong())).willReturn(Optional.of(customer));

        // when
        Customer result = customerService.getAvailableCustomer(1L);

        // then
        assertAll(() -> assertEquals(customer.customerId(), result.customerId()),
                () -> assertEquals(customer.customerName(), result.customerName()),
                () -> assertEquals(customer.pointBalance(), result.pointBalance()));
    }

    /**
     * 유효 고객 검증 모두 통과 시
     */
    @Test
    void meetsIfCustomerValid_when_pass_all() {
        // given
        Customer customer = Customer.create(1L, "김항해", BigDecimal.valueOf(10000));

        // when
        boolean isValid = customerService.meetsIfCustomerValid(customer);

        // then
        assertThat(isValid).isTrue();
    }

    /**
     * 유효 고객 검증 고객_ID 이상할 시
     */
    @Test
    void meetsIfCustomerValid_when_customerId_is_wrong() {
        // given
        Customer customer = Customer.create(-1L, "김항해", BigDecimal.valueOf(10000));

        // when
        boolean isValid = customerService.meetsIfCustomerValid(customer);

        // then
        assertThat(isValid).isFalse();
    }

    /**
     * 유효 고객 검증 고객 명 이상할 시
     */
    @Test
    void meetsIfCustomerValid_when_customerName_is_wrong() {
        // given
        Customer customer = Customer.create(1L, "", BigDecimal.valueOf(10000));

        // when
        boolean isValid = customerService.meetsIfCustomerValid(customer);

        // then
        assertThat(isValid).isFalse();
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
     * 고객 포인트 충전 정보 검증 모두 통과 시
     */
    @Test
    void meetsIfPointValidBeforeCharge_when_pass_all() {
        // given
        CustomerPointHistory customerPointHistory = CustomerPointHistory.create(
                1L,
                BigDecimal.valueOf(30000),
                PointType.CHARGE,
                null
        );

        // when
        boolean isValid = customerService.meetsIfPointValidBeforeCharge(customerPointHistory);

        // then
        assertThat(isValid).isTrue();
    }

    /**
     * 고객 포인트 충전 정보 검증 충전 금액 이상할 시
     */
    @Test
    void meetsIfPointValidBeforeCharge_when_pointAmount_is_wrong() {
        // given
        CustomerPointHistory customerPointHistory = CustomerPointHistory.create(
                1L,
                BigDecimal.valueOf(-30000),
                PointType.CHARGE,
                null
        );

        // when
        boolean isValid = customerService.meetsIfPointValidBeforeCharge(customerPointHistory);

        // then
        assertThat(isValid).isFalse();
    }

    /**
     * 고객 포인트 충전 정보 검증 포인트 구분 이상할 시
     */
    @Test
    void meetsIfPointValidBeforeCharge_when_pointType_is_wrong() {
        // given
        CustomerPointHistory customerPointHistory = CustomerPointHistory.create(
                1L,
                BigDecimal.valueOf(30000),
                PointType.USE,
                null
        );

        // when
        boolean isValid = customerService.meetsIfPointValidBeforeCharge(customerPointHistory);

        // then
        assertThat(isValid).isFalse();
    }

    /**
     * 고객 포인트 내역 저장
     */
    @Test
    void saveCustomerPointHistory() {
        // given
        CustomerPointHistory customerPointHistory = CustomerPointHistory.create(
                1L,
                BigDecimal.valueOf(30000),
                PointType.CHARGE,
                null
        );
        given(customerRepository.saveCustomerPointHistory(any(CustomerPointHistory.class))).willReturn(customerPointHistory);

        // when
        CustomerPointHistory result = customerService.saveCustomerPointHistory(customerPointHistory);

        // then
        assertAll(() -> assertEquals(customerPointHistory.customerId(), result.customerId()),
                () -> assertEquals(customerPointHistory.pointAmount(), result.pointAmount()),
                () -> assertEquals(customerPointHistory.pointType(), result.pointType()));
    }

    /**
     * 고객포인트 잔액 충분 검증 충분 시
     */
    @Test
    void meetsIfPointBalanceSufficient_when_pass() {
        // given
        BigDecimal pointBalance = BigDecimal.valueOf(30000);
        BigDecimal targetAmount = BigDecimal.valueOf(20000);
        given(customerRepository.sumCustomerPointBalanceByCustomerId(anyLong())).willReturn(pointBalance);

        // when
        BigDecimal result = customerService.getCustomerPointBalance(1L);
        boolean isValid = customerService.meetsIfPointBalanceSufficient(1L, targetAmount);

        // then
        assertThat(result).isEqualTo(pointBalance);
        assertThat(isValid).isTrue();
    }

    /**
     * 고객포인트 잔액 충분 검증 부족 시
     */
    @Test
    void meetsIfPointBalanceSufficient_when_insufficient() {
        // given
        BigDecimal pointBalance = BigDecimal.valueOf(30000);
        BigDecimal targetAmount = BigDecimal.valueOf(50000);
        given(customerRepository.sumCustomerPointBalanceByCustomerId(anyLong())).willReturn(pointBalance);

        // when
        BigDecimal result = customerService.getCustomerPointBalance(1L);
        boolean isValid = customerService.meetsIfPointBalanceSufficient(1L, targetAmount);

        // then
        assertThat(result).isEqualTo(pointBalance);
        assertThat(isValid).isFalse();
    }

    /**
     * 고객 포인트 사용
     */
    @Test
    void useCustomerPoint() {
        // given
        CustomerPointHistory customerPointHistory = CustomerPointHistory.create(
                1L,
                BigDecimal.valueOf(30000),
                PointType.USE,
                null
        );
        given(customerRepository.saveCustomerPointHistory(any(CustomerPointHistory.class))).willReturn(customerPointHistory);

        // when
        CustomerPointHistory result = customerService.saveCustomerPointHistory(customerPointHistory);

        // then
        assertAll(() -> assertEquals(customerPointHistory.customerId(), result.customerId()),
                () -> assertEquals(customerPointHistory.pointAmount(), result.pointAmount()),
                () -> assertEquals(customerPointHistory.pointType(), result.pointType()));
    }

}