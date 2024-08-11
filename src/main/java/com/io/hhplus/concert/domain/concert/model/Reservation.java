package com.io.hhplus.concert.domain.concert.model;

import com.io.hhplus.concert.common.GlobalConstants;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.common.utils.DateUtils;
import lombok.Builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
public record Reservation(
        Long reservationId,
        Long customerId,
        String bookerName,
        List<Ticket> tickets,
        Date createdAt,
        Date modifiedAt,
        Date deletedAt
) {

    public static Reservation reserve(Long customerId, String bookerName) {
        return Reservation.builder()
                .customerId(customerId)
                .bookerName(bookerName)
                .build();
    }

    public Reservation addNewTickets(Concert concert, ConcertSchedule concertSchedule, ConcertSeat concertSeat, List<String> seatNumbers) {
        List<Ticket> tickets = new ArrayList<>();
        seatNumbers.forEach(seatNumber -> tickets.add(Ticket.reserve(concert, concertSchedule, concertSeat, seatNumber)));
        return Reservation.builder()
                .customerId(this.customerId)
                .bookerName(this.bookerName)
                .tickets(tickets)
                .build();
    }

    public Reservation addConfirmedTickets(List<Ticket> tickets) {
        if (tickets.isEmpty()) throw new CustomException(ResponseMessage.TICKET_NOT_FOUND);
        List<Ticket> addedTickets = new ArrayList<>();
        tickets.forEach(ticket -> addedTickets.add(ticket.confirmReservation(this)));
        return Reservation.builder()
                .reservationId(this.reservationId)
                .customerId(this.customerId)
                .bookerName(this.bookerName)
                .tickets(addedTickets)
                .build();
    }

    public boolean isNotDeleted() {
        return this.deletedAt == null;
    }

    public boolean isAbleToPay() {
        return DateUtils.calculateDuration(DateUtils.getSysDate(), this.createdAt) < GlobalConstants.MAX_DURATION_OF_ACTIVE_QUEUE_IN_SECONDS;
    }
}
