package com.io.hhplus.concert.application.customer.facade;

import com.io.hhplus.concert.application.customer.dto.CustomerPointHistoryResponse;
import com.io.hhplus.concert.application.customer.dto.CustomerPointRequest;
import com.io.hhplus.concert.application.customer.dto.CustomerResponse;
import com.io.hhplus.concert.common.enums.PointType;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.IllegalArgumentCustomException;
import com.io.hhplus.concert.common.exceptions.ResourceNotFoundCustomException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CustomerIntegrationTest {

    @Autowired
    private CustomerFacade customerFacade;

    /**
     * 고객 포인트 조회 - 잘못된 고객 ID
     */
    @Test
    void getCustomerPoint_customerId_is_wrong() {
        // given
        Long customerId = null;

        // when - then
        assertThatThrownBy(() -> customerFacade.getCustomerPoint(customerId))
                .isInstanceOf(IllegalArgumentCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.INVALID);

    }

    /**
     * 고객 포인트 조회 - 없는 고객
     */
    @Test
    void getCustomerPoint_customer_not_exists() {
        // given
        long customerId = 10001;

        // when - then
        assertThatThrownBy(() -> customerFacade.getCustomerPoint(customerId))
                .isInstanceOf(ResourceNotFoundCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);

    }

    /**
     * 고객 포인트 조회 - 유효
     */
    @Test
    void getCustomerPoint_when_valid() {
        // given
        long customerId = 1;

        // when
        CustomerResponse result = customerFacade.getCustomerPoint(customerId);

        // then
        assertAll(() -> assertEquals(customerId, result.getCustomer().customerId()),
                () -> assertTrue(BigDecimal.ZERO.compareTo(result.getCustomer().pointBalance()) <= 0));
    }

    /**
     * 고객 포인트 충전 - 유효
     */
    @Test
    void chargeCustomerPoint_when_valid() {
        // given
        CustomerPointRequest request = new CustomerPointRequest(
                1L, BigDecimal.valueOf(10000)
        );

        // when
        CustomerPointHistoryResponse result = customerFacade.chargeCustomerPoint(request);

        // then
        assertAll(() -> assertEquals(request.getCustomerId(), result.getCustomer().customerId()),
                () -> assertEquals(PointType.CHARGE, result.getCustomerPointHistory().pointType()),
                () -> assertEquals(request.getPointAmount(), result.getCustomerPointHistory().pointAmount()));

    }

    /**
     * 고객 포인트 충전 - 존재하지 않는 고객
     */
    @Test
    void chargeCustomerPoint_when_customer_not_exists() {
        // given
        CustomerPointRequest request = new CustomerPointRequest(
                99999L, BigDecimal.valueOf(10000)
        );

        // when - then
        assertThatThrownBy(() -> customerFacade.chargeCustomerPoint(request))
                .isInstanceOf(ResourceNotFoundCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
    }

    /**
     * 고객 포인트 충전 - 유효하지 않은 충전금액
     */
    @Test
    void chargeCustomerPoint_when_chargeAmount_not_valid() {
        // given
        CustomerPointRequest request = new CustomerPointRequest(
                1L, BigDecimal.valueOf(10000).negate()
        );

        // when - then
        assertThatThrownBy(() -> customerFacade.chargeCustomerPoint(request))
                .isInstanceOf(IllegalArgumentCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.INVALID);
    }

}