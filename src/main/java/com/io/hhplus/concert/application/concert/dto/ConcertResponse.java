package com.io.hhplus.concert.application.concert.dto;

import com.io.hhplus.concert.domain.concert.model.Concert;
import lombok.*;

@Getter
@AllArgsConstructor
public class ConcertResponse {
    private Concert concert;
}
