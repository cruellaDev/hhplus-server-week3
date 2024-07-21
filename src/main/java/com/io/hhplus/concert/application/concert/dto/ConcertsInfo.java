package com.io.hhplus.concert.application.concert.dto;

import com.io.hhplus.concert.domain.concert.service.model.ConcertModel;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ConcertsInfo {
    private final List<ConcertModel> concerts;
}
