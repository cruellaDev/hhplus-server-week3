package com.io.hhplus.concert.domain.customer.service;

import com.io.hhplus.concert.common.enums.PointType;
import com.io.hhplus.concert.domain.customer.CustomerValidator;
import com.io.hhplus.concert.domain.customer.CustomerRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Disabled
class CustomerValidatorTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerValidator customerValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isAvailableCustomerId_is_null() {
        // given
        Long customerId = null;

        // when
        boolean isValid = customerValidator.isAvailableCustomerId(customerId);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableCustomerId_is_negative() {
        // given
        Long customerId = -1L;

        // when
        boolean isValid = customerValidator.isAvailableCustomerId(customerId);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableCustomerId() {
        // given
        Long customerId = 1L;

        // when
        boolean isValid = customerValidator.isAvailableCustomerId(customerId);

        // then
        assertTrue(isValid);
    }

    @Test
    void isAvailableCustomerName_is_null() {
        // given
        String customerName = null;

        // when
        boolean isValid = customerValidator.isAvailableCustomerName(customerName);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableCustomerName_is_blank() {
        // given
        String customerName = " ";

        // when
        boolean isValid = customerValidator.isAvailableCustomerName(customerName);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableCustomerName() {
        // given
        String customerName = "고객명";

        // when
        boolean isValid = customerValidator.isAvailableCustomerName(customerName);

        // then
        assertTrue(isValid);
    }

    @Test
    void isSufficientPointAmount_is_null() {
        // given
        BigDecimal pointAmount = null;

        // when
        boolean isValid = customerValidator.isSufficientPointAmount(pointAmount);

        // then
        assertFalse(isValid);
    }

    @Test
    void isSufficientPointAmount_is_negative() {
        // given
        BigDecimal pointAmount = BigDecimal.valueOf(30000).negate();

        // when
        boolean isValid = customerValidator.isSufficientPointAmount(pointAmount);

        // then
        assertFalse(isValid);
    }

    @Test
    void isSufficientPointAmount() {
        // given
        BigDecimal pointAmount = BigDecimal.valueOf(30000);

        // when
        boolean isValid = customerValidator.isSufficientPointAmount(pointAmount);

        // then
        assertTrue(isValid);
    }

    @Test
    void isAvailablePointTypeWhenCharge_is_null() {
        // given
        PointType pointType = null;

        // when
        boolean isValid = customerValidator.isAvailablePointTypeWhenCharge(pointType);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailablePointTypeWhenCharge_is_not_charge() {
        // given
        PointType pointType = PointType.USE;

        // when
        boolean isValid = customerValidator.isAvailablePointTypeWhenCharge(pointType);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailablePointTypeWhenCharge() {
        // given
        PointType pointType = PointType.CHARGE;

        // when
        boolean isValid = customerValidator.isAvailablePointTypeWhenCharge(pointType);

        // then
        assertTrue(isValid);
    }

//    @Test
//    void checkIfCustomerValid_customerId_is_wrong() {
//        // given
//        CustomerModel customerModel = CustomerModel.create(-1L, "김항해", BigDecimal.valueOf(10000));
//
//        // when - then
//        assertThrows(CustomException.class, () -> customerValidator.checkIfCustomerValid(customerModel));
//    }
//
//    @Test
//    void checkIfCustomerValid_customerName_is_wrong() {
//        // given
//        CustomerModel customerModel = CustomerModel.create(1L, "", BigDecimal.valueOf(10000));
//
//        // when - then
//        assertThrows(CustomException.class, () -> customerValidator.checkIfCustomerValid(customerModel));
//    }
//
//    @Test
//    void checkIfPointValidBeforeCharge_pointAmount_is_wrong() {
//        // given
//        CustomerPointHistoryModel customerPointHistoryModel = CustomerPointHistoryModel.create(
//                1L,
//                BigDecimal.valueOf(-30000),
//                PointType.CHARGE,
//                null
//        );
//
//        // when - then
//        assertThrows(CustomException.class, () -> customerValidator.checkIfPointValidBeforeCharge(customerPointHistoryModel));
//    }
//
//    @Test
//    void checkIfPointValidBeforeCharge_pointType_is_wrong() {
//        // given
//        CustomerPointHistoryModel customerPointHistoryModel = CustomerPointHistoryModel.create(
//                1L,
//                BigDecimal.valueOf(30000),
//                PointType.USE,
//                null
//        );
//
//        // when - then
//        assertThrows(CustomException.class, () -> customerValidator.checkIfPointValidBeforeCharge(customerPointHistoryModel));
//    }
//
//    @Test
//    void meetsIfPointBalanceSufficient_not_sufficient() {
//        // given
//        BigDecimal pointBalance = BigDecimal.valueOf(30000);
//        BigDecimal targetAmount = BigDecimal.valueOf(50000);
//        given(customerRepository.sumCustomerPointBalanceByCustomerId(anyLong())).willReturn(pointBalance);
//
//        // when - then
//        boolean isSufficient = customerValidator.meetsIfPointBalanceSufficient(1L, targetAmount);
//
//        // then
//        assertThat(isSufficient).isFalse();
//    }
//
//    @Test
//    void meetsIfPointBalanceSufficient() {
//        // given
//        BigDecimal pointBalance = BigDecimal.valueOf(30000);
//        BigDecimal targetAmount = BigDecimal.valueOf(20000);
//        given(customerRepository.sumCustomerPointBalanceByCustomerId(anyLong())).willReturn(pointBalance);
//
//        // when
//        boolean isSufficient = customerValidator.meetsIfPointBalanceSufficient(1L, targetAmount);
//
//        // then
//        assertThat(isSufficient).isTrue();
//    }
//
//    @Test
//    void validateCustomer_token_is_wrong() {
//        // given
//        CustomerModel customerModel = CustomerModel.noContents();
//        given(customerRepository.findAvailableOneById(anyLong())).willReturn(Optional.of(customerModel));
//
//        // when
//        assertThrows(CustomException.class, () -> customerValidator.validateCustomer(null));
//
//    }
//
//    @Test
//    void validateCustomer_customer_is_Empty() {
//        // given
//        CustomerModel customerModel = CustomerModel.noContents();
//        given(customerRepository.findAvailableOneById(anyLong())).willReturn(Optional.of(customerModel));
//
//        // when
//        assertThrows(CustomException.class, () -> customerValidator.validateCustomer("0"));
//    }
}