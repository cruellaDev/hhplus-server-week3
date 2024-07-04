package com.io.hhplus.concert.presentation.reservation.dto.request;

import com.io.hhplus.concert.presentation.concert.dto.request.PostPerformanceRequestBody;
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
