package com.io.hhplus.concert.application.waiting.facade;

import com.io.hhplus.concert.application.waiting.dto.WaitingResponse;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.IllegalArgumentCustomException;
import com.io.hhplus.concert.common.exceptions.ResourceNotFoundCustomException;
import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.service.CustomerService;
import com.io.hhplus.concert.domain.waiting.model.WaitingQueue;
import com.io.hhplus.concert.domain.waiting.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class WaitingFacade {

    private final CustomerService customerService;
    private final WaitingService waitingService;

    /**
     * 대기열 토큰 발급 (대기열 진입)
     * @param customerId 고객_ID
     * @return 대기열 정보
     */
    @Transactional
    public WaitingResponse publishWaitingToken(Long customerId) {
        if (!Customer.isAvailableCustomerId(customerId)) {
            throw new IllegalArgumentCustomException("고객 ID 값이 잘못 되었습니다.", ResponseMessage.NOT_AVAILABLE);
        }

        Customer customer;
        WaitingQueue waitingQueue;
        boolean isExists;
        boolean isValid;

        // 고객 조회
        customer = customerService.getAvailableCustomer(customerId);
        // 고객 검증
        isValid = customerService.meetsIfCustomerValid(customer);
        if (!isValid) {
            throw new ResourceNotFoundCustomException("존재하지 않는 고객입니다.", ResponseMessage.NOT_AVAILABLE);
        }

        // 대기열 조회
        waitingQueue = waitingService.getActiveWaitingTokenByCustomerId(customerId);

        // 대기열 존재 여부
        isExists = waitingService.meetsIfActiveWaitingQueueExists(waitingQueue);

        // 토큰 활성 시간 확인
        isValid = waitingService.meetsIfActiveWaitingQueueInTimeLimits(300L, waitingQueue.createdAt());

        // 유효 대기열 없을 시 대기열 진입
        if (!isExists && !isValid) {
            waitingQueue = waitingService.enterWaitingQueue(customerId);
        }

        // 대기인원 조회
        Long numberOfWaiting = waitingService.getTheNumberOfWaiting();

        // 대기순번 조회
        Long waitingNumber = waitingService.getWaitingNumberByWaitingId(waitingQueue.waitingId());

        // 잔여 활성 시간 계산
        Long remainingTime = TimeUnit.MILLISECONDS.toSeconds((new Date()).getTime() - waitingQueue.createdAt().getTime());

        // 대기열 정보 반환
        return new WaitingResponse(customer.customerId(), numberOfWaiting, waitingNumber, waitingQueue.waitingStatus(), remainingTime);
    }

    /**
     * 대기열 토큰 조회
     * @param customerId 고객_ID
     * @return 대기열 정보
     */
    public WaitingResponse getWaitingToken(Long customerId) {
        if (!Customer.isAvailableCustomerId(customerId)) {
            throw new IllegalArgumentCustomException("고객 ID 값이 잘못 되었습니다.", ResponseMessage.NOT_AVAILABLE);
        }

        Customer customer;
        WaitingQueue waitingQueue;
        boolean isExists;
        boolean isValid;

        // 고객 조회
        customer = customerService.getAvailableCustomer(customerId);
        // 고객 검증
        isValid = customerService.meetsIfCustomerValid(customer);
        if (!isValid) {
            throw new ResourceNotFoundCustomException("존재하지 않는 고객입니다.", ResponseMessage.NOT_AVAILABLE);
        }

        // 대기열 조회
        waitingQueue = waitingService.getActiveWaitingTokenByCustomerId(customerId);

        // 대기열 존재 여부
        isExists = waitingService.meetsIfActiveWaitingQueueExists(waitingQueue);

        // 토큰 활성 시간 확인
        isValid = waitingService.meetsIfActiveWaitingQueueInTimeLimits(300L, waitingQueue.createdAt());

        // 유효 대기열 없을 시
        if (!isExists && !isValid) {
            throw new ResourceNotFoundCustomException("대기열 진입 정보가 존재하지 않습니다.", ResponseMessage.NOT_FOUND);
        }

        // 대기인원 조회
        Long numberOfWaiting = waitingService.getTheNumberOfWaiting();

        // 대기순번 조회
        Long waitingNumber = waitingService.getWaitingNumberByWaitingId(waitingQueue.waitingId());

        // 잔여 활성 시간 계산
        Long remainingTime = TimeUnit.MILLISECONDS.toSeconds((new Date()).getTime() - waitingQueue.createdAt().getTime());

        // 대기열 정보 반환
        return new WaitingResponse(customer.customerId(), numberOfWaiting, waitingNumber, waitingQueue.waitingStatus(), remainingTime);
    }

    /**
     * 만료된 토큰 제거
     */
    public void removeAllExpiredToken() {
        waitingService.removeAllExpiredWaitingQueueToken();
    }
}
