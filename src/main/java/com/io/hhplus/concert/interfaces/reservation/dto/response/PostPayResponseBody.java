package com.io.hhplus.concert.interfaces.reservation.dto.response;

import com.io.hhplus.concert.interfaces.reservation.dto.PayDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostPayResponseBody {
    Long customerId;
    List<PostPayedTicketsResponseBody> tickets;
    List<PayDto> pay;

    public PostPayResponseBody(Long customerId, List<PostPayedTicketsResponseBody> tickets, List<PayDto> pay) {
        this.customerId = customerId;
        this.tickets = tickets;
        this.pay = pay;
    }
}
