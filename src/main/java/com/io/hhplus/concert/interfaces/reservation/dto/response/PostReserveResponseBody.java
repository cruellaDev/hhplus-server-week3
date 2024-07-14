package com.io.hhplus.concert.interfaces.reservation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostReserveResponseBody {
    PostReservationResponseBody reservation;
    List<PostTicketResponseBody> tickets;

    public PostReserveResponseBody(PostReservationResponseBody reservation, List<PostTicketResponseBody> tickets) {
        this.reservation = reservation;
        this.tickets = tickets;
    }
}
