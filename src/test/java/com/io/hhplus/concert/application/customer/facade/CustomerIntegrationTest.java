package com.io.hhplus.concert.application.customer.facade;

import com.io.hhplus.concert.application.customer.dto.CustomerInfoWithCustomerPointHistory;
import com.io.hhplus.concert.application.customer.dto.CustomerInfo;
import com.io.hhplus.concert.common.enums.PointType;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
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
    void 고객_포인트_조회_고객ID_null_일_시_예외_처리() {
        // given
        Long customerId = null;

        // when - then
        assertThatThrownBy(() -> customerFacade.getCustomerPoint(customerId))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.INVALID);

    }

    /**
     * 고객 포인트 조회 - 없는 고객
     */
    @Test
    void 고객_포인트_조회_없는_고객일_시_예외_처리() {
        // given
        long customerId = 10001;

        // when - then
        assertThatThrownBy(() -> customerFacade.getCustomerPoint(customerId))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);

    }

    /**
     * 고객 포인트 조회 - 유효
     */
    @Test
    void 고객_포인트_조회_존재하는_고객일_시_고객_정보_및_포인트_정보를_반환한다() {
        // given
        long customerId = 1;

        // when
        CustomerInfo result = customerFacade.getCustomerPoint(customerId);

        // then
        assertAll(() -> assertEquals(customerId, result.getCustomer().customerId()),
                () -> assertTrue(BigDecimal.ZERO.compareTo(result.getCustomer().pointBalance()) <= 0));
    }

    /**
     * 고객 포인트 충전 - 유효
     */
    @Test
    void 고객_포인트_충전_유효한_고객일_시_포인트를_충전하고_고객_정보_및_포인트_정보를_반환한다() {
        // given
        Long customerId = 1L;
        BigDecimal pointAmount = BigDecimal.valueOf(10000);

        // when
        CustomerInfoWithCustomerPointHistory result = customerFacade.chargeCustomerPoint(customerId, pointAmount);

        // then
        assertAll(() -> assertEquals(customerId, result.getCustomer().customerId()),
                () -> assertEquals(PointType.CHARGE, result.getCustomerPointHistory().pointType()),
                () -> assertEquals(pointAmount, result.getCustomerPointHistory().pointAmount()));

    }

    /**
     * 고객 포인트 충전 - 고객 ID 이상할 시
     */
    @Test
    void 고객_포인트_충전_고객_ID_null_일_시_예외_처리() {
        // given
        Long customerId = null;
        BigDecimal pointAmount = BigDecimal.valueOf(10000);

        // when - then
        assertThatThrownBy(() -> customerFacade.chargeCustomerPoint(customerId, pointAmount))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.INVALID);
    }

    /**
     * 고객 포인트 충전 - 존재하지 않는 고객
     */
    @Test
    void 고객_포인트_충전_존재하지_않는_고객_일_시_예외_처리() {
        // given
        Long customerId = 99999L;
        BigDecimal pointAmount = BigDecimal.valueOf(10000);

        // when - then
        assertThatThrownBy(() -> customerFacade.chargeCustomerPoint(customerId, pointAmount))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
    }

    /**
     * 고객 포인트 충전 - 유효하지 않은 충전금액
     */
    @Test
    void 고객_포인트_충전_유효하지_않은_충전_금액_일_시_예외_처리() {
        // given
        Long customerId = 1L;
        BigDecimal pointAmount = BigDecimal.valueOf(10000).negate();

        // when - then
        assertThatThrownBy(() -> customerFacade.chargeCustomerPoint(customerId, pointAmount))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
    }

}