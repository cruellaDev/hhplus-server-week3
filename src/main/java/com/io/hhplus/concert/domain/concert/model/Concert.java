package com.io.hhplus.concert.domain.concert.model;

import com.io.hhplus.concert.common.enums.ConcertStatus;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.concert.ConcertCommand;
import lombok.Builder;

import java.util.Date;

@Builder
public record Concert(
        Long concertId,
        String concertName,
        String artistName,
        Date bookBeginAt,
        Date bookEndAt,
        ConcertStatus concertStatus,
        Date createdAt,
        Date modifiedAt,
        Date deletedAt
) {
    public static Concert create() {
        return Concert.builder().build();
    }

    public Concert register(ConcertCommand.RegisterConcertCommand command) {
        if (command.getConcertName() == null || command.getConcertName().isBlank()) {
            throw new CustomException(ResponseMessage.CONCERT_INVALID, "콘서트 명이 존재하지 않습니다.");
        }
        if (command.getArtistName() == null || command.getArtistName().isBlank()) {
            throw new CustomException(ResponseMessage.CONCERT_INVALID, "아티스트 명이 존재하지 않습니다.");
        }
        if (command.getBookBeginAt() == null || command.getBookEndAt() == null) {
            throw new CustomException(ResponseMessage.CONCERT_INVALID, "예매 기간은 필수값입니다.");
        }
        if (command.getBookEndAt().before(DateUtils.getSysDate())) {
            throw new CustomException(ResponseMessage.CONCERT_INVALID, "예매 종료 일시는 현재 일시보다 이후여야 합니다.");
        }
        if (command.getBookEndAt().before(command.getBookBeginAt())) {
            throw new CustomException(ResponseMessage.CONCERT_INVALID, "예매 시작 일시는 예매 종료 일시보다 이전이어야 합니다.");
        }
        return Concert.builder()
                .concertName(command.getConcertName())
                .artistName(command.getArtistName())
                .bookBeginAt(command.getBookBeginAt())
                .bookEndAt(command.getBookEndAt())
                .concertStatus(ConcertStatus.WAITING)
                .build();
    }

    public boolean isAbleToBook() {
        Date currentDate = DateUtils.getSysDate();
        return this.bookBeginAt.before(currentDate) && this.bookEndAt.after(currentDate);
    }

    public boolean isAvailableConcertStatus() {
        return this.concertStatus.isAvailable();
    }

    public boolean isNotDeleted() {
        return this.deletedAt == null;
    }

    public void checkValid() {
        if (isAbleToBook() && isAvailableConcertStatus() && isNotDeleted()) {
            return;
        }
        throw new CustomException(ResponseMessage.CONCERT_INVALID);
    }
}
