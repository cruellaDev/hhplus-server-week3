package com.io.hhplus.concert.presentation.reservation.dto.request;

import com.io.hhplus.concert.presentation.reservation.dto.PayDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostPayRequestBody {
    Long customerId;
    Long reservationId;
    PayDto pay;
}
