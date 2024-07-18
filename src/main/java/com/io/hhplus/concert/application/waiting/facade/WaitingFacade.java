package com.io.hhplus.concert.application.waiting.facade;

import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.customer.service.CustomerValidator;
import com.io.hhplus.concert.domain.waiting.service.WaitingValidator;
import com.io.hhplus.concert.application.waiting.dto.WaitingResultInfo;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.domain.customer.service.model.CustomerModel;
import com.io.hhplus.concert.domain.customer.service.CustomerService;
import com.io.hhplus.concert.domain.waiting.service.model.WaitingQueueModel;
import com.io.hhplus.concert.domain.waiting.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class WaitingFacade {

    private final CustomerService customerService;
    private final WaitingService waitingService;

    private final CustomerValidator customerValidator;
    private final WaitingValidator waitingValidator;

    /**
     * 대기열 토큰 발급 (대기열 진입)
     * @param customerId 고객_ID
     * @return 대기열 정보
     */
    @Transactional
    public WaitingResultInfo publishWaitingToken(Long customerId) {
        if (customerValidator.isNotAvailableCustomerId(customerId)) {
            throw new CustomException(ResponseMessage.INVALID, "고객 ID 값이 잘못 되었습니다.");
        }

        CustomerModel customerModel;
        WaitingQueueModel waitingQueueModel;
        boolean isExists;
        boolean isValid;

        // 고객 조회
        customerModel = customerService.getAvailableCustomer(customerId);
        // 고객 검증
        customerValidator.checkIfCustomerValid(customerModel);

        // 대기열 조회
        waitingQueueModel = waitingService.getActiveWaitingTokenByCustomerId(customerId);

        // 대기열 존재 여부
        isExists = waitingValidator.meetsIfActiveWaitingQueueExists(waitingQueueModel);

        // 토큰 활성 시간 확인
        isValid = waitingValidator.meetsIfActiveWaitingQueueInTimeLimits(300L, waitingQueueModel.createdAt());

        // 유효 대기열 없을 시 대기열 진입
        if (!isExists && !isValid) {
            waitingQueueModel = waitingService.enterWaitingQueue(customerId);
        }

        // 대기인원 조회
        Long numberOfWaiting = waitingService.getTheNumberOfWaiting();

        // 대기순번 조회
        Long waitingNumber = waitingService.getWaitingNumberByWaitingId(waitingQueueModel.waitingId());

        // 잔여 활성 시간 계산
        Date now = DateUtils.getSysDate();
        Long remainingTime = DateUtils.calculateDuration(now, waitingQueueModel.createdAt());

        // 대기열 정보 반환
        return new WaitingResultInfo(customerModel.customerId(), numberOfWaiting, waitingNumber, waitingQueueModel.waitingStatus(), remainingTime);
    }

    /**
     * 대기열 토큰 조회
     * @param customerId 고객_ID
     * @return 대기열 정보
     */
    public WaitingResultInfo getWaitingToken(Long customerId) {
        if (customerValidator.isNotAvailableCustomerId(customerId)) {
            throw new CustomException(ResponseMessage.INVALID, "고객 ID 값이 잘못 되었습니다.");
        }

        CustomerModel customerModel;
        WaitingQueueModel waitingQueueModel;
        boolean isExists;
        boolean isValid;

        // 고객 조회
        customerModel = customerService.getAvailableCustomer(customerId);
        // 고객 검증
        customerValidator.checkIfCustomerValid(customerModel);

        // 대기열 조회
        waitingQueueModel = waitingService.getActiveWaitingTokenByCustomerId(customerId);

        // 대기열 존재 여부
        isExists = waitingValidator.meetsIfActiveWaitingQueueExists(waitingQueueModel);

        // 토큰 활성 시간 확인
        isValid = waitingValidator.meetsIfActiveWaitingQueueInTimeLimits(300L, waitingQueueModel.createdAt());

        // 유효 대기열 없을 시
        if (!isExists && !isValid) {
            throw new CustomException(ResponseMessage.NOT_FOUND, "대기열 진입 정보가 존재하지 않습니다.");
        }

        // 대기인원 조회
        Long numberOfWaiting = waitingService.getTheNumberOfWaiting();

        // 대기순번 조회
        Long waitingNumber = waitingService.getWaitingNumberByWaitingId(waitingQueueModel.waitingId());

        // 잔여 활성 시간 계산
        Date now = DateUtils.getSysDate();
        Long remainingTime = DateUtils.calculateDuration(now, waitingQueueModel.createdAt());

        // 대기열 정보 반환
        return new WaitingResultInfo(customerModel.customerId(), numberOfWaiting, waitingNumber, waitingQueueModel.waitingStatus(), remainingTime);
    }

    /**
     * 만료된 토큰 제거
     */
    public void removeAllExpiredToken() {
        waitingService.removeAllExpiredWaitingQueueToken();
    }
}
