package com.io.hhplus.concert.interfaces.reservation.dto.request;

import com.io.hhplus.concert.interfaces.concert.dto.request.PostPerformanceRequestBody;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostReserveRequestBody {
    PostReserverRequestBody reserver;
    Long concertId;
    List<PostPerformanceRequestBody> performances;
}
