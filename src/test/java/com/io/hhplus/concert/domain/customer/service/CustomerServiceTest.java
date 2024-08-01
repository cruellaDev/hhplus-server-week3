package com.io.hhplus.concert.domain.customer.service;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.domain.customer.CustomerService;
import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.CustomerRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Disabled
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    /**
//     * 고객 포인트 잔액 조회
//     */
//    @Test
//    void 고객이_존재하지_않으면_에러를_반환한다() {
//        // given
//        long customerId = 9999;
//        given(customerRepository.findAvailableCustomer(anyLong())).willReturn(Optional.empty());
//
//        // when - then
//        assertThatThrownBy(() -> customerService.getCustomerPointBalance(customerId))
//                .isInstanceOf(CustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.CUSTOMER_NOT_FOUND);
//    }
//
//    /**
//     * 고객 포인트 잔액 조회
//     */
//    @Test
//    void 고객이_존재하면_잔액_정보를_반환한다() {
//        // given
//        Customer customer = Customer.builder()
//                .customerId(1L)
//                .customerUuid(UUID.randomUUID())
//                .customerName("김항해")
//                .pointBalance(BigDecimal.ZERO)
//                .build();
//        given(customerRepository.findAvailableCustomer(anyLong())).willReturn(Optional.of(customer));
//
//        // when
//        CustomerPointBalanceServiceResponse result = customerService.getCustomerPointBalance(1L);
//
//        // then
//        assertAll(() -> assertEquals(customer.customerId(), result.customerId()),
//                () -> assertEquals(customer.pointBalance(), result.pointBalance()));
//    }
//
//    /**
//     * 고객 포인트 충전
//     */
//    @Test
//    void 고객_포인트_충전_시_고객이_존재하지_않으면_에러를_반환한다() {
//        // given
//        long customerId = 9999;
//        BigDecimal pointAmount = BigDecimal.valueOf(10000);
//        given(customerRepository.findAvailableCustomer(anyLong())).willReturn(Optional.empty());
//
//        // when - then
//        assertThatThrownBy(() -> customerService.chargeCustomerPoint(customerId, pointAmount))
//                .isInstanceOf(CustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.CUSTOMER_NOT_FOUND);
//    }
//
//    /**
//     * 고객 포인트 충전
//     */
//    @Test
//    void 고객_포인트_충전_시_고객이_존재하지만_충전_금액이_0원이면_에러를_반환한다() {
//        // given
//        long customerId = 1;
//        BigDecimal pointAmount = BigDecimal.ZERO;
//        Customer customer = Customer.builder()
//                .customerId(customerId)
//                .customerUuid(UUID.randomUUID())
//                .customerName("김항해")
//                .pointBalance(BigDecimal.ZERO)
//                .build();
//        given(customerRepository.findAvailableCustomer(anyLong())).willReturn(Optional.of(customer));
//
//        // when - then
//        assertThatThrownBy(() -> customerService.chargeCustomerPoint(customerId, pointAmount))
//                .isInstanceOf(CustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.INVALID);
//        assertThatThrownBy(() -> customerService.chargeCustomerPoint(customerId, pointAmount))
//                .isInstanceOf(CustomException.class)
//                .extracting("messageDetail")
//                .isEqualTo("포인트 충전 금액은 0보다 커야 합니다.");
//    }
//
//    /**
//     * 고객 포인트 충전
//     */
//    @Test
//    void 고객_포인트_충전_시_고객이_존재하지만_충전_금액이_음수이면_에러를_반환한다() {
//        // given
//        long customerId = 1;
//        BigDecimal pointAmount = BigDecimal.valueOf(-50000);
//        Customer customer = Customer.builder()
//                .customerId(customerId)
//                .customerUuid(UUID.randomUUID())
//                .customerName("김항해")
//                .pointBalance(BigDecimal.ZERO)
//                .build();
//        given(customerRepository.findAvailableCustomer(anyLong())).willReturn(Optional.of(customer));
//
//        // when - then
//        assertThatThrownBy(() -> customerService.chargeCustomerPoint(customerId, pointAmount))
//                .isInstanceOf(CustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.INVALID);
//        assertThatThrownBy(() -> customerService.chargeCustomerPoint(customerId, pointAmount))
//                .isInstanceOf(CustomException.class)
//                .extracting("messageDetail")
//                .isEqualTo("포인트 충전 금액은 0보다 커야 합니다.");
//    }
//
//    /**
//     * 고객 포인트 사용
//     */
//    @Test
//    void 고객_포인트_사용_시_고객이_존재하지_않으면_에러를_반환한다() {
//        // given
//        long customerId = 9999;
//        BigDecimal pointAmount = BigDecimal.valueOf(10000);
//        given(customerRepository.findAvailableCustomer(anyLong())).willReturn(Optional.empty());
//
//        // when - then
//        assertThatThrownBy(() -> customerService.useCustomerPoint(customerId, pointAmount))
//                .isInstanceOf(CustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.CUSTOMER_NOT_FOUND);
//    }
//
//    /**
//     * 고객 포인트 사용
//     */
//    @Test
//    void 고객_포인트_사용_시_고객이_존재하지만_사용_금액이_0원이면_에러를_반환한다() {
//        // given
//        long customerId = 1;
//        BigDecimal pointAmount = BigDecimal.ZERO;
//        Customer customer = Customer.builder()
//                .customerId(customerId)
//                .customerUuid(UUID.randomUUID())
//                .customerName("김항해")
//                .pointBalance(BigDecimal.ZERO)
//                .build();
//        given(customerRepository.findAvailableCustomer(anyLong())).willReturn(Optional.of(customer));
//
//        // when - then
//        assertThatThrownBy(() -> customerService.useCustomerPoint(customerId, pointAmount))
//                .isInstanceOf(CustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.INVALID);
//        assertThatThrownBy(() -> customerService.useCustomerPoint(customerId, pointAmount))
//                .isInstanceOf(CustomException.class)
//                .extracting("messageDetail")
//                .isEqualTo("포인트 사용 금액은 0보다 커야 합니다.");
//    }
//
//    /**
//     * 고객 포인트 사용
//     */
//    @Test
//    void 고객_포인트_사용_시_고객이_존재하지만_사용_금액이_음수이면_에러를_반환한다() {
//        // given
//        long customerId = 1;
//        BigDecimal pointAmount = BigDecimal.valueOf(-40000);
//        Customer customer = Customer.builder()
//                .customerId(customerId)
//                .customerUuid(UUID.randomUUID())
//                .customerName("김항해")
//                .pointBalance(BigDecimal.ZERO)
//                .build();
//        given(customerRepository.findAvailableCustomer(anyLong())).willReturn(Optional.of(customer));
//
//        // when - then
//        assertThatThrownBy(() -> customerService.useCustomerPoint(customerId, pointAmount))
//                .isInstanceOf(CustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.INVALID);
//        assertThatThrownBy(() -> customerService.useCustomerPoint(customerId, pointAmount))
//                .isInstanceOf(CustomException.class)
//                .extracting("messageDetail")
//                .isEqualTo("포인트 사용 금액은 0보다 커야 합니다.");
//    }
//
//    /**
//     * 고객 포인트 사용
//     */
//    @Test
//    void 고객_포인트_사용_시_고객이_존재하지만_사용_금액이_포인트_잔액을_초과하면_에러를_반환한다() {
//        // given
//        long customerId = 1;
//        BigDecimal pointAmount = BigDecimal.valueOf(30000);
//        Customer customer = Customer.builder()
//                .customerId(customerId)
//                .customerUuid(UUID.randomUUID())
//                .customerName("김항해")
//                .pointBalance(BigDecimal.ZERO)
//                .build();
//        given(customerRepository.findAvailableCustomer(anyLong())).willReturn(Optional.of(customer));
//
//        // when - then
//        assertThatThrownBy(() -> customerService.useCustomerPoint(customerId, pointAmount))
//                .isInstanceOf(CustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.OUT_OF_BUDGET);
//        assertThatThrownBy(() -> customerService.useCustomerPoint(customerId, pointAmount))
//                .isInstanceOf(CustomException.class)
//                .extracting("messageDetail")
//                .isEqualTo("포인트 잔액이 부족합니다.");
//    }

}