package com.io.hhplus.concert.application.reservation.dto;

import com.io.hhplus.concert.common.enums.ReceiveMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReserverInfo {
    private String reserverName;
    private ReceiveMethod receiveMethod;
    private String receiverName;
    private String receiverPostcode;
    private String receiverBaseAddress;
    private String receiverDetailAddress;
}
