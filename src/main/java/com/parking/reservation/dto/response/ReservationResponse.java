package com.parking.reservation.dto.response;

import com.parking.reservation.enums.ReservationStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReservationResponse {
    private Long id;
    private Long slotId;
    private String vehicleNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal cost;
    private ReservationStatus status;
}
