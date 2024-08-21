package com.io.hhplus.concert.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {
    OK("SUCCESS"),
    FAIL("요청에 실패하였습니다."),
    INVALID("데이터가 유효하지 않습니다."),
    CONCERT_INVALID("콘서트 데이터가 유효하지 않습니다."),
    CONCERT_SCHEDULE_INVALID("공연 일정 데이터가 유효하지 않습니다."),
    CONCERT_SEAT_INVALID("공연 좌석 데이터가 유효하지 않습니다."),
    PAYMENT_AMOUNT_INVALID("결제 금액이 유효하지 않습니다."),
    QUEUE_STATUS_INVALID("대기 상태가 유효하지 않습니다."),
    ALREADY_RESERVED("예약 데이터가 이미 존재합니다."),
    NOT_AVAILABLE("사용할 수 없는 데이터입니다."),
    CONCERT_NOT_AVAILABLE("이용할 수 없는 콘서트 정보입니다."),
    CUSTOMER_NOT_AVAILABLE("이용할 수 없는 사용자 정보입니다."),
    RESERVATION_NOT_AVAILABLE("이용할 수 없는 예약 정보입니다."),
    NOT_FOUND("데이터가 존재하지 않습니다."),
    CONCERT_NOT_FOUND("존재하지 않는 콘서트 정보입니다."),
    CONCERT_SCHEDULE_NOT_FOUND("존재하지 않는 콘서트 일정 정보입니다."),
    CONCERT_SEAT_NOT_FOUND("존재하지 않는 콘서트 좌석 정보입니다."),
    CUSTOMER_NOT_FOUND("존재하지 않는 사용자 정보입니다."),
    WAITING_NOT_FOUND("존재하지 않는 대기열 정보입니다."),
    RESERVATION_NOT_FOUND("존재하지 않는 예약 정보입니다."),
    TICKET_NOT_FOUND("존재하지 않는 예매 티켓 정보입니다."),
    SEAT_TAKEN("이미 선점된 좌석입니다"),
    TOKEN_NOT_FOUNT("존재하지 않는 토큰입니다."),
    OUT_OF_TIME("시간이 초과되었습니다."),
    RESERVATION_TIMED_OUT("예약 가능 요청 시간이 초과되었습니다."),
    PAYMENT_OUT_OF_TIME("결제요청 시간이 초과되었습니다."),
    RESERVATION_OUT_OF_TIME("결제요청 시간이 초과되었습니다."),
    WAITING_TOKEN_EXPIRED("대기 토큰이 만료되었습니다."),
    OUT_OF_BUDGET("금액이 부족합니다."),
    UNKNOWN("알 수 없는 에러"),
    ALREADY_PUBLISHED("이미 발행된 데이터가 존재합니다.")
    ;

    private final String messageDetail;
}
