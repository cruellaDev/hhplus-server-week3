package com.io.hhplus.concert.domain.reservation.service;

import com.io.hhplus.concert.common.enums.ReceiveMethod;
import com.io.hhplus.concert.common.enums.ReservationStatus;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.reservation.service.model.ReservationModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class ReservationValidator {

    public boolean isAvailableReservationId(Long reservationId) {
        return reservationId != null && reservationId.compareTo(0L) > 0;
    }

    public boolean isNotAvailableReservationId(Long reservationId) {
        return !this.isAvailableReservationId(reservationId);
    }

    public boolean isAvailableReserverName(String reserverName) {
        return reserverName != null && !reserverName.isBlank() && !reserverName.equals("N/A");
    }

    public boolean isNotAvailableReserverName(String reserverName) {
        return !this.isAvailableReserverName(reserverName);
    }

    public boolean isAvailableReceiverName(String receiverName) {
        return receiverName != null && !receiverName.isBlank() && !receiverName.equals("N/A");
    }

    public boolean isNotAvailableReceiverName(String receiverName) {
        return !this.isAvailableReceiverName(receiverName);
    }

    public boolean isAvailableReceiveMethod(ReceiveMethod receiveMethod) {
        return receiveMethod != null;
    }

    public boolean isNotAvailableReceiveMethod(ReceiveMethod receiveMethod) {
        return !this.isAvailableReceiveMethod(receiveMethod);
    }

    public boolean isAvailableReservationStatus(ReservationStatus reservationStatus) {
        return reservationStatus != null;
    }

    public boolean isAbleToPayReservationStatus(ReservationStatus reservationStatus) {
        return this.isAvailableReservationStatus(reservationStatus) && reservationStatus.equals(ReservationStatus.REQUESTED);
    }

    public boolean isNotAbleToPayReservationStatus(ReservationStatus reservationStatus) {
        return !this.isAbleToPayReservationStatus(reservationStatus);
    }

    public boolean isInPayableDuration(Long seconds, Long targetSeconds) {
        return seconds != null && targetSeconds != null && seconds.compareTo(targetSeconds) > 0;
    }

    public boolean isNotInPayableDuration(Long seconds, Long targetSeconds) {
        return !this.isInPayableDuration(seconds, targetSeconds);
    }

    /**
     * 예약 정보 결제 유효 기간 내 존재 여부 확인
     * @param seconds 유효 기준 기간 단위 (초)
     * @param reservationStatusChangedAt 예약상태변경일시
     * @return 예약 정보 결제 유효 기간 내 존재 여부 (예약상태변경일시와 현재일시의 차가 유효 기준 기간 단위 이내 이면 true)
     */
    public boolean meetsIfAbleToPayInTimeLimits(Long seconds, Date reservationStatusChangedAt) {
        Date currentDate = new Date();
        long targetSeconds = DateUtils.calculateDuration(currentDate, reservationStatusChangedAt);
        return this.isInPayableDuration(seconds, targetSeconds);
    }

    /**
     * 예약 유효성 확인
     * @param reservationModel 예약 정보
     * @return 예약 유효 여부
     */
    public boolean meetsIfReservationAvailable(ReservationModel reservationModel) {
        return reservationModel != null
                && this.isAvailableReservationId(reservationModel.reservationId())
                && this.isAvailableReserverName(reservationModel.reserverName())
                && this.isAvailableReceiverName(reservationModel.receiverName())
                && this.isAvailableReceiveMethod(reservationModel.receiveMethod())
                && this.isAvailableReservationStatus(reservationModel.reservationStatus());
    }

}
